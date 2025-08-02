package UMC_7th.Closit.domain.user.service.social;

import UMC_7th.Closit.domain.user.entity.OAuthUserInfo;
import UMC_7th.Closit.global.apiPayload.exception.handler.UserHandler;
import UMC_7th.Closit.global.common.SocialLoginType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
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
@DisplayName("구글 소셜 로그인 서비스 테스트")
class GoogleSocialLoginServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private GoogleIdTokenVerifier verifier;

    @Mock
    private GoogleIdToken googleIdToken;

    @Mock
    private GoogleIdToken.Payload payload;

    @InjectMocks
    private GoogleSocialLoginService googleSocialLoginService;

    private final String mockIdToken = "mock_google_id_token";
    private final String mockAccessToken = "mock_google_access_token";
    private final String mockAccessTokenResponseBody = """
            {
                "id": "123456789",
                "email": "test@gmail.com",
                "name": "Test User",
                "picture": "https://lh3.googleusercontent.com/test"
            }
            """;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(googleSocialLoginService, "googleClientId", "test-client-id");
        ReflectionTestUtils.setField(googleSocialLoginService, "restTemplate", restTemplate);
        ReflectionTestUtils.setField(googleSocialLoginService, "objectMapper", objectMapper);
    }

    @Test
    @DisplayName("지원하는 소셜 로그인 타입 반환 테스트")
    void getSupportedType() {
        // when
        SocialLoginType result = googleSocialLoginService.getSupportedType();

        // then
        assertThat(result).isEqualTo(SocialLoginType.GOOGLE);
    }

    @Test
    @DisplayName("ID Token으로 사용자 정보 조회 성공")
    void getUserInfoFromIdToken_Success() throws Exception {
        // given
        when(payload.getSubject()).thenReturn("123456789");
        when(payload.getEmail()).thenReturn("test@gmail.com");
        when(payload.get("name")).thenReturn("Test User");
        when(payload.getIssuer()).thenReturn("https://accounts.google.com");
        when(payload.getAudience()).thenReturn("test-client-id");

        when(googleIdToken.getPayload()).thenReturn(payload);

        try (MockedStatic<GoogleIdTokenVerifier> mockedVerifier = mockStatic(GoogleIdTokenVerifier.class)) {
            GoogleIdTokenVerifier.Builder mockBuilder = mock(GoogleIdTokenVerifier.Builder.class);
            when(mockBuilder.setAudience(any())).thenReturn(mockBuilder);
            when(mockBuilder.build()).thenReturn(verifier);

            mockedVerifier.when(() -> new GoogleIdTokenVerifier.Builder(any(), any())).thenReturn(mockBuilder);            when(verifier.verify(mockIdToken)).thenReturn(googleIdToken);

            // when
            OAuthUserInfo result = googleSocialLoginService.getUserInfoFromIdToken(mockIdToken);

            // then
            assertThat(result).isNotNull();
            assertThat(result.getProvider()).isEqualTo(SocialLoginType.GOOGLE);
            assertThat(result.providerId()).isEqualTo("123456789");
            assertThat(result.getEmail()).isEqualTo("test@gmail.com");
            assertThat(result.getName()).isEqualTo("Test User");
        }
    }

    @Test
    @DisplayName("ID Token이 유효하지 않을 때 예외 발생")
    void getUserInfoFromIdToken_InvalidToken() throws Exception {
        // given
        try (MockedStatic<GoogleIdTokenVerifier> mockedVerifier = mockStatic(GoogleIdTokenVerifier.class)) {
            GoogleIdTokenVerifier.Builder mockBuilder = mock(GoogleIdTokenVerifier.Builder.class);
            when(mockBuilder.setAudience(any())).thenReturn(mockBuilder);
            when(mockBuilder.build()).thenReturn(verifier);

            when(verifier.verify(mockIdToken)).thenReturn(null); // 유효하지 않은 토큰

            // when & then
            assertThatThrownBy(() -> googleSocialLoginService.getUserInfoFromIdToken(mockIdToken))
                    .isInstanceOf(UserHandler.class);
        }
    }

    @Test
    @DisplayName("Access Token으로 사용자 정보 조회 성공")
    void getUserInfoFromAccessToken_Success() throws Exception {
        // given
        ResponseEntity<String> mockResponse = new ResponseEntity<>(mockAccessTokenResponseBody, HttpStatus.OK);
        JsonNode mockJsonNode = new ObjectMapper().readTree(mockAccessTokenResponseBody);

        when(restTemplate.exchange(
                eq("https://www.googleapis.com/oauth2/v2/userinfo"),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(String.class)
        )).thenReturn(mockResponse);

        when(objectMapper.readTree(mockAccessTokenResponseBody)).thenReturn(mockJsonNode);

        // when
        OAuthUserInfo result = googleSocialLoginService.getUserInfoFromAccessToken(mockAccessToken);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getProvider()).isEqualTo(SocialLoginType.GOOGLE);
        assertThat(result.providerId()).isEqualTo("123456789");
        assertThat(result.getEmail()).isEqualTo("test@gmail.com");
        assertThat(result.getName()).isEqualTo("Test User");

        // verify
        verify(restTemplate).exchange(
                eq("https://www.googleapis.com/oauth2/v2/userinfo"),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(String.class)
        );
        verify(objectMapper).readTree(mockAccessTokenResponseBody);
    }

    @Test
    @DisplayName("Access Token이 유효하지 않을 때 예외 발생")
    void getUserInfoFromAccessToken_InvalidToken() {
        // given
        ResponseEntity<String> mockResponse = new ResponseEntity<>("", HttpStatus.UNAUTHORIZED);

        when(restTemplate.exchange(
                eq("https://www.googleapis.com/oauth2/v2/userinfo"),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(String.class)
        )).thenReturn(mockResponse);

        // when & then
        assertThatThrownBy(() -> googleSocialLoginService.getUserInfoFromAccessToken(mockAccessToken))
                .isInstanceOf(UserHandler.class);
    }

    @Test
    @DisplayName("Access Token API 호출 중 예외 발생 시 UserHandler 예외 발생")
    void getUserInfoFromAccessToken_ApiException() {
        // given
        when(restTemplate.exchange(
                eq("https://www.googleapis.com/oauth2/v2/userinfo"),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(String.class)
        )).thenThrow(new RuntimeException("Network error"));

        // when & then
        assertThatThrownBy(() -> googleSocialLoginService.getUserInfoFromAccessToken(mockAccessToken))
                .isInstanceOf(UserHandler.class);
    }
}
