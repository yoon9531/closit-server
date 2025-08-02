package UMC_7th.Closit.domain.user.service.social;

import UMC_7th.Closit.domain.user.entity.GoogleUserInfo;
import UMC_7th.Closit.domain.user.entity.OAuthUserInfo;
import UMC_7th.Closit.global.apiPayload.code.status.ErrorStatus;
import UMC_7th.Closit.global.apiPayload.exception.handler.UserHandler;
import UMC_7th.Closit.global.common.SocialLoginType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;


@Service
@RequiredArgsConstructor
@Slf4j
public class GoogleSocialLoginService implements SocialLoginService {

    @Value("${oauth.google.client-id}")
    private String googleClientId;

    private static final JsonFactory jsonFactory = new GsonFactory();
    private static final String GOOGLE_USERINFO_URL = "https://www.googleapis.com/oauth2/v2/userinfo";

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public SocialLoginType getSupportedType() {
        return SocialLoginType.GOOGLE;
    }

    @Override
    public OAuthUserInfo getUserInfoFromIdToken(String idToken) {
        try {
            // Verifier Object 생성
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    new NetHttpTransport(), jsonFactory)
                    .setAudience(Collections.singletonList(googleClientId))
                    .build();

            GoogleIdToken googleIdToken = verifier.verify(idToken);

            if (googleIdToken == null) {
                log.debug("Invalid ID token: {}", idToken);
                throw new UserHandler(ErrorStatus.INVALID_TOKEN);
            }

            GoogleIdToken.Payload payload = googleIdToken.getPayload();

            // Issuer 체크
            if (!"accounts.google.com".equals(payload.getIssuer()) &&
                    !"https://accounts.google.com".equals(payload.getIssuer())) {
                log.debug("Invalid issuer: {}", payload.getIssuer());
                throw new UserHandler(ErrorStatus.INVALID_TOKEN);
            }

            // aud (Audience) 검증
            if (!googleClientId.equals(payload.getAudience())) {
                log.debug("Invalid audience: {}", payload.getAudience());
                throw new UserHandler(ErrorStatus.INVALID_TOKEN);
            }

            return new GoogleUserInfo(payload);
        } catch (Exception e) {
            log.error("Failed to verify Google ID token", e);
            throw new UserHandler(ErrorStatus.INVALID_TOKEN);
        }
    }

    @Override
    public OAuthUserInfo getUserInfoFromAccessToken(String accessToken) {
        try {
            // HTTP 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(accessToken);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            // Google UserInfo API 호출
            ResponseEntity<String> response = restTemplate.exchange(
                    GOOGLE_USERINFO_URL,
                    HttpMethod.GET,
                    entity,
                    String.class
            );

            if (!response.getStatusCode().is2xxSuccessful()) {
                log.debug("Failed to get user info from Google: {}", response.getStatusCode());
                throw new UserHandler(ErrorStatus.INVALID_TOKEN);
            }

            // JSON 파싱
            JsonNode userInfoJson = objectMapper.readTree(response.getBody());

            return new GoogleUserInfo(userInfoJson);
        } catch (Exception e) {
            log.error("Failed to get user info from Google access token", e);
            throw new UserHandler(ErrorStatus.INVALID_TOKEN);
        }
    }
}
