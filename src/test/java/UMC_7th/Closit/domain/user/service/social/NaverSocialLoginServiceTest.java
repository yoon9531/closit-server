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
@DisplayName("네이버 소셜 로그인 서비스 테스트")
class NaverSocialLoginServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private NaverSocialLoginService naverSocialLoginService;

    private final String mockAccessToken = "mock_naver_access_token";
    private final String mockSuccessResponseBody = """
            {
                "resultcode": "00",
                "message": "success",
                "response": {
                    "id": "naver123456789",
                    "email": "test@naver.com",
                    "name": "홍길동",
                    "nickname": "길동이"
                }
            }
            """;

    private final String mockErrorResponseBody = """
            {
                "resultcode": "024",
                "message": "invalid access token"
            }
            """;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(naverSocialLoginService, "restTemplate", restTemplate);
        ReflectionTestUtils.setField(naverSocialLoginService, "objectMapper", objectMapper);
    }

    @Test
    @DisplayName("지원하는 소셜 로그인 타입 반환 테스트")
    void getSupportedType() {
        // when
        SocialLoginType result = naverSocialLoginService.getSupportedType();

        // then
        assertThat(result).isEqualTo(SocialLoginType.NAVER);
    }

    @Test
    @DisplayName("ID Token으로 사용자 정보 조회 시 예외 발생")
    void getUserInfoFromIdToken_ThrowsException() {
        // when & then
        assertThatThrownBy(() -> naverSocialLoginService.getUserInfoFromIdToken("dummy_id_token"))
                .isInstanceOf(UserHandler.class);
    }

    @Test
    @DisplayName("Access Token으로 사용자 정보 조회 성공")
    void getUserInfoFromAccessToken_Success() throws Exception {
        // given
        ResponseEntity<String> mockResponse = new ResponseEntity<>(mockSuccessResponseBody, HttpStatus.OK);
        JsonNode mockJsonNode = new ObjectMapper().readTree(mockSuccessResponseBody);

        when(restTemplate.exchange(
                eq("https://openapi.naver.com/v1/nid/me"),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(String.class)
        )).thenReturn(mockResponse);

        when(objectMapper.readTree(mockSuccessResponseBody)).thenReturn(mockJsonNode);

        // when
        OAuthUserInfo result = naverSocialLoginService.getUserInfoFromAccessToken(mockAccessToken);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getProvider()).isEqualTo(SocialLoginType.NAVER);
        assertThat(result.providerId()).isEqualTo("naver123456789");
        assertThat(result.getEmail()).isEqualTo("test@naver.com");
        assertThat(result.getName()).isEqualTo("홍길동");

        // verify
        verify(restTemplate).exchange(
                eq("https://openapi.naver.com/v1/nid/me"),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(String.class)
        );
        verify(objectMapper).readTree(mockSuccessResponseBody);
    }

    @Test
    @DisplayName("Access Token이 유효하지 않을 때 예외 발생 - HTTP 상태 코드")
    void getUserInfoFromAccessToken_InvalidToken_HttpStatus() {
        // given
        ResponseEntity<String> mockResponse = new ResponseEntity<>("", HttpStatus.UNAUTHORIZED);

        when(restTemplate.exchange(
                eq("https://openapi.naver.com/v1/nid/me"),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(String.class)
        )).thenReturn(mockResponse);

        // when & then
        assertThatThrownBy(() -> naverSocialLoginService.getUserInfoFromAccessToken(mockAccessToken))
                .isInstanceOf(UserHandler.class);
    }

    @Test
    @DisplayName("네이버 API 응답 코드가 에러일 때 예외 발생")
    void getUserInfoFromAccessToken_NaverApiError() throws Exception {
        // given
        ResponseEntity<String> mockResponse = new ResponseEntity<>(mockErrorResponseBody, HttpStatus.OK);
        JsonNode mockJsonNode = new ObjectMapper().readTree(mockErrorResponseBody);

        when(restTemplate.exchange(
                eq("https://openapi.naver.com/v1/nid/me"),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(String.class)
        )).thenReturn(mockResponse);

        when(objectMapper.readTree(mockErrorResponseBody)).thenReturn(mockJsonNode);

        // when & then
        assertThatThrownBy(() -> naverSocialLoginService.getUserInfoFromAccessToken(mockAccessToken))
                .isInstanceOf(UserHandler.class);
    }

    @Test
    @DisplayName("API 호출 중 예외 발생 시 UserHandler 예외 발생")
    void getUserInfoFromAccessToken_ApiException() {
        // given
        when(restTemplate.exchange(
                eq("https://openapi.naver.com/v1/nid/me"),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(String.class)
        )).thenThrow(new RuntimeException("Network error"));

        // when & then
        assertThatThrownBy(() -> naverSocialLoginService.getUserInfoFromAccessToken(mockAccessToken))
                .isInstanceOf(UserHandler.class);
    }

    @Test
    @DisplayName("닉네임만 있고 이름이 없는 경우")
    void getUserInfoFromAccessToken_OnlyNickname() throws Exception {
        // given
        String responseWithOnlyNickname = """
                {
                    "resultcode": "00",
                    "message": "success",
                    "response": {
                        "id": "naver123456789",
                        "email": "test@naver.com",
                        "nickname": "길동이"
                    }
                }
                """;

        ResponseEntity<String> mockResponse = new ResponseEntity<>(responseWithOnlyNickname, HttpStatus.OK);
        JsonNode mockJsonNode = new ObjectMapper().readTree(responseWithOnlyNickname);

        when(restTemplate.exchange(
                eq("https://openapi.naver.com/v1/nid/me"),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(String.class)
        )).thenReturn(mockResponse);

        when(objectMapper.readTree(responseWithOnlyNickname)).thenReturn(mockJsonNode);

        // when
        OAuthUserInfo result = naverSocialLoginService.getUserInfoFromAccessToken(mockAccessToken);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("길동이");
    }

    @Test
    @DisplayName("이름도 닉네임도 없는 경우 기본값 반환")
    void getUserInfoFromAccessToken_NoNameAndNickname() throws Exception {
        // given
        String responseWithoutName = """
                {
                    "resultcode": "00",
                    "message": "success",
                    "response": {
                        "id": "naver123456789",
                        "email": "test@naver.com"
                    }
                }
                """;

        ResponseEntity<String> mockResponse = new ResponseEntity<>(responseWithoutName, HttpStatus.OK);
        JsonNode mockJsonNode = new ObjectMapper().readTree(responseWithoutName);

        when(restTemplate.exchange(
                eq("https://openapi.naver.com/v1/nid/me"),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(String.class)
        )).thenReturn(mockResponse);

        when(objectMapper.readTree(responseWithoutName)).thenReturn(mockJsonNode);

        // when
        OAuthUserInfo result = naverSocialLoginService.getUserInfoFromAccessToken(mockAccessToken);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("네이버 사용자");
    }
}
