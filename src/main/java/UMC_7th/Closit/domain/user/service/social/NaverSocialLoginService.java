package UMC_7th.Closit.domain.user.service.social;

import UMC_7th.Closit.domain.user.entity.NaverUserInfo;
import UMC_7th.Closit.domain.user.entity.OAuthUserInfo;
import UMC_7th.Closit.global.apiPayload.code.status.ErrorStatus;
import UMC_7th.Closit.global.apiPayload.exception.handler.UserHandler;
import UMC_7th.Closit.global.common.SocialLoginType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class NaverSocialLoginService implements SocialLoginService {

    private static final String NAVER_USERINFO_URL = "https://openapi.naver.com/v1/nid/me";

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public SocialLoginType getSupportedType() {
        return SocialLoginType.NAVER;
    }

    @Override
    public OAuthUserInfo getUserInfoFromIdToken(String idToken) {
        throw new UserHandler(ErrorStatus.NOT_SUPPORTED_SOCIAL_LOGIN);
    }

    @Override
    public OAuthUserInfo getUserInfoFromAccessToken(String accessToken) {
        try {
            // HTTP 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(accessToken);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            // 네이버 UserInfo API 호출
            ResponseEntity<String> response = restTemplate.exchange(
                    NAVER_USERINFO_URL,
                    HttpMethod.GET,
                    entity,
                    String.class
            );

            if (!response.getStatusCode().is2xxSuccessful()) {
                log.debug("Failed to get user info from Naver: {}", response.getStatusCode());
                throw new UserHandler(ErrorStatus.INVALID_TOKEN);
            }

            // JSON 파싱
            JsonNode userInfoJson = objectMapper.readTree(response.getBody());

            // 네이버 API 응답 코드 확인
            if (!"00".equals(userInfoJson.get("resultcode").asText())) {
                log.debug("Naver API returned error: {}", userInfoJson.get("message").asText());
                throw new UserHandler(ErrorStatus.INVALID_TOKEN);
            }

            return new NaverUserInfo(userInfoJson);
        } catch (Exception e) {
            log.error("Failed to get user info from Naver access token", e);
            throw new UserHandler(ErrorStatus.INVALID_TOKEN);
        }
    }
}
