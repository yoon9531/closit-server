package UMC_7th.Closit.domain.user.service;

import org.junit.jupiter.api.Test;
import UMC_7th.Closit.domain.user.dto.JwtResponse;
import UMC_7th.Closit.domain.user.dto.OAuthLoginRequestDTO;
import UMC_7th.Closit.domain.user.entity.OAuthUserInfo;
import UMC_7th.Closit.domain.user.entity.User;
import UMC_7th.Closit.domain.user.repository.UserRepository;
import UMC_7th.Closit.domain.user.service.social.SocialLoginFactory;
import UMC_7th.Closit.domain.user.service.social.SocialLoginService;
import UMC_7th.Closit.global.common.SocialLoginType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("소셜 로그인 통합 테스트")
class UserAuthServiceSocialLoginTest {

    @Mock
    private SocialLoginFactory socialLoginFactory;

    @Mock
    private SocialLoginService socialLoginService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserUtil userUtil;

    @Mock
    private OAuthUserInfo oAuthUserInfo;

    @Mock
    private User user;

    @InjectMocks
    private UserAuthServiceImpl userAuthService;

    private OAuthLoginRequestDTO requestDTO;
    private JwtResponse expectedJwtResponse;

    @BeforeEach
    void setUp() {
        requestDTO = new OAuthLoginRequestDTO(
                OAuthLoginRequestDTO.TokenType.ACCESS_TOKEN,
                "mock_access_token"
        );

        expectedJwtResponse = JwtResponse.builder()
                .accessToken("mock_access_token")
                .refreshToken("mock_refresh_token")
                .build();

        when(oAuthUserInfo.getEmail()).thenReturn("test@example.com");
        when(oAuthUserInfo.getName()).thenReturn("테스트사용자");
        when(oAuthUserInfo.getProvider()).thenReturn(SocialLoginType.KAKAO);
    }

    @Test
    @DisplayName("Access Token으로 소셜 로그인 성공 - 기존 회원")
    void socialLogin_AccessToken_ExistingUser() {
        // given
        when(socialLoginFactory.getService(SocialLoginType.KAKAO))
                .thenReturn(socialLoginService);
        when(socialLoginService.getUserInfoFromAccessToken("mock_access_token"))
                .thenReturn(oAuthUserInfo);
        when(userRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(user));
        when(userUtil.issueJwtTokens(user))
                .thenReturn(expectedJwtResponse);

        // when
        JwtResponse result = userAuthService.socialLogin(SocialLoginType.KAKAO, requestDTO);

        // then
        assertThat(result).isEqualTo(expectedJwtResponse);

        verify(socialLoginFactory).getService(SocialLoginType.KAKAO);
        verify(socialLoginService).getUserInfoFromAccessToken("mock_access_token");
        verify(userRepository).findByEmail("test@example.com");
        verify(userUtil).issueJwtTokens(user);
    }

    @Test
    @DisplayName("ID Token으로 소셜 로그인 성공 - 신규 회원")
    void socialLogin_IdToken_NewUser() {
        // given
        OAuthLoginRequestDTO idTokenRequest = new OAuthLoginRequestDTO(
                OAuthLoginRequestDTO.TokenType.ID_TOKEN,
                "mock_id_token"
        );

        when(socialLoginFactory.getService(SocialLoginType.GOOGLE))
                .thenReturn(socialLoginService);
        when(socialLoginService.getUserInfoFromIdToken("mock_id_token"))
                .thenReturn(oAuthUserInfo);
        when(userRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.empty());
        when(userUtil.issueJwtTokens(any(User.class)))
                .thenReturn(expectedJwtResponse);

        // registerNewUser 메서드 모킹을 위해 spy 사용
        UserAuthServiceImpl spyService = spy(userAuthService);
        doReturn(user).when(spyService).registerNewUser(oAuthUserInfo);

        // when
        JwtResponse result = spyService.socialLogin(SocialLoginType.GOOGLE, idTokenRequest);

        // then
        assertThat(result).isEqualTo(expectedJwtResponse);

        verify(socialLoginFactory).getService(SocialLoginType.GOOGLE);
        verify(socialLoginService).getUserInfoFromIdToken("mock_id_token");
        verify(userRepository).findByEmail("test@example.com");
        verify(spyService).registerNewUser(oAuthUserInfo);
        verify(userUtil).issueJwtTokens(any(User.class));
    }

    @Test
    @DisplayName("잘못된 토큰 타입으로 소셜 로그인 시 예외 발생")
    void socialLogin_InvalidTokenType() {
        // given
        OAuthLoginRequestDTO invalidRequest = new OAuthLoginRequestDTO();
        invalidRequest.setTokenType(null);
        invalidRequest.setToken("mock_token");

        // when & then
        assertThatThrownBy(() -> userAuthService.socialLogin(SocialLoginType.GOOGLE, invalidRequest))
                .isInstanceOf(Exception.class);
    }
}
