package UMC_7th.Closit.domain.user.service.social;

import UMC_7th.Closit.domain.user.entity.KakaoUserInfo;
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

/**
 * 카카오 소셜 로그인 서비스 구현체
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class KakaoSocialLoginService implements SocialLoginService {

    private static final String KAKAO_USERINFO_URL = "https://kapi.kakao.com/v2/user/me";

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public SocialLoginType getSupportedType() {
        return SocialLoginType.KAKAO;
    }

    @Override
    public OAuthUserInfo getUserInfoFromIdToken(String idToken) {
        // 카카오는 ID Token을 지원하지 않음
        throw new UserHandler(ErrorStatus.NOT_SUPPORTED_SOCIAL_LOGIN);
    }

    @Override
    public OAuthUserInfo getUserInfoFromAccessToken(String accessToken) {
        try {
            // HTTP 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(accessToken);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            // 카카오 UserInfo API 호출
            ResponseEntity<String> response = restTemplate.exchange(
                    KAKAO_USERINFO_URL,
                    HttpMethod.GET,
                    entity,
                    String.class
            );

            if (!response.getStatusCode().is2xxSuccessful()) {
                log.debug("Failed to get user info from Kakao: {}", response.getStatusCode());
                throw new UserHandler(ErrorStatus.INVALID_TOKEN);
            }

            // JSON 파싱
            JsonNode userInfoJson = objectMapper.readTree(response.getBody());

            return new KakaoUserInfo(userInfoJson);
        } catch (Exception e) {
            log.error("Failed to get user info from Kakao access token", e);
            throw new UserHandler(ErrorStatus.INVALID_TOKEN);
        }
    }
}
