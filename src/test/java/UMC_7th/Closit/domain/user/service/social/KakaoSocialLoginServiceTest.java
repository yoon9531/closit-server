package UMC_7th.Closit.domain.user.service.social;

import UMC_7th.Closit.domain.user.entity.OAuthUserInfo;
import UMC_7th.Closit.global.apiPayload.exception.handler.UserHandler;
import UMC_7th.Closit.global.common.SocialLoginType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("카카오 소셜 로그인 서비스 테스트")
class KakaoSocialLoginServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private KakaoSocialLoginService kakaoSocialLoginService;

    private final String mockAccessToken = "mock_kakao_access_token";
    private final String mockResponseBody = """
            {
                "id": "123456789",
                "kakao_account": {
                    "email": "test@kakao.com",
                    "profile": {
                        "nickname": "테스트사용자"
                    }
                },
                "properties": {
                    "nickname": "테스트사용자"
                }
            }
            """;

    @BeforeEach
    void setUp() {
        // private final 필드들을 Mock으로 교체
        ReflectionTestUtils.setField(kakaoSocialLoginService, "restTemplate", restTemplate);
        ReflectionTestUtils.setField(kakaoSocialLoginService, "objectMapper", objectMapper);
    }

    @Test
    @DisplayName("지원하는 소셜 로그인 타입 반환 테스트")
    void getSupportedType() {
        // when
        SocialLoginType result = kakaoSocialLoginService.getSupportedType();

        // then
        assertThat(result).isEqualTo(SocialLoginType.KAKAO);
    }

    @Test
    @DisplayName("ID Token으로 사용자 정보 조회 시 예외 발생")
    void getUserInfoFromIdToken_ThrowsException() {
        // when & then
        assertThatThrownBy(() -> kakaoSocialLoginService.getUserInfoFromIdToken("dummy_id_token"))
                .isInstanceOf(UserHandler.class);
    }

    @Test
    @DisplayName("Access Token으로 사용자 정보 조회 성공")
    void getUserInfoFromAccessToken_Success() throws Exception {
        // given
        ResponseEntity<String> mockResponse = new ResponseEntity<>(mockResponseBody, HttpStatus.OK);
        JsonNode mockJsonNode = new ObjectMapper().readTree(mockResponseBody);

        when(restTemplate.exchange(
                eq("https://kapi.kakao.com/v2/user/me"),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(String.class)
        )).thenReturn(mockResponse);

        when(objectMapper.readTree(mockResponseBody)).thenReturn(mockJsonNode);

        // when
        OAuthUserInfo result = kakaoSocialLoginService.getUserInfoFromAccessToken(mockAccessToken);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getProvider()).isEqualTo(SocialLoginType.KAKAO);
        assertThat(result.providerId()).isEqualTo("123456789");
        assertThat(result.getEmail()).isEqualTo("test@kakao.com");
        assertThat(result.getName()).isEqualTo("테스트사용자");

        // verify
        verify(restTemplate).exchange(
                eq("https://kapi.kakao.com/v2/user/me"),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(String.class)
        );
        verify(objectMapper).readTree(mockResponseBody);
    }

    @Test
    @DisplayName("Access Token이 유효하지 않을 때 예외 발생")
    void getUserInfoFromAccessToken_InvalidToken() {
        // given
        ResponseEntity<String> mockResponse = new ResponseEntity<>("", HttpStatus.UNAUTHORIZED);

        when(restTemplate.exchange(
                eq("https://kapi.kakao.com/v2/user/me"),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(String.class)
        )).thenReturn(mockResponse);

        // when & then
        assertThatThrownBy(() -> kakaoSocialLoginService.getUserInfoFromAccessToken(mockAccessToken))
                .isInstanceOf(UserHandler.class);
    }

    @Test
    @DisplayName("API 호출 중 예외 발생 시 UserHandler 예외 발생")
    void getUserInfoFromAccessToken_ApiException() {
        // given
        when(restTemplate.exchange(
                eq("https://kapi.kakao.com/v2/user/me"),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(String.class)
        )).thenThrow(new RuntimeException("Network error"));

        // when & then
        assertThatThrownBy(() -> kakaoSocialLoginService.getUserInfoFromAccessToken(mockAccessToken))
                .isInstanceOf(UserHandler.class);
    }
}
