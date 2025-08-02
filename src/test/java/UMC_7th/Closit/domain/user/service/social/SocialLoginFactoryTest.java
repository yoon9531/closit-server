package UMC_7th.Closit.domain.user.service.social;

import UMC_7th.Closit.global.apiPayload.exception.GeneralException;
import UMC_7th.Closit.global.common.SocialLoginType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("소셜 로그인 팩토리 테스트")
class SocialLoginFactoryTest {

    @Mock
    private SocialLoginService googleService;

    @Mock
    private SocialLoginService kakaoService;

    @Mock
    private SocialLoginService naverService;

    private SocialLoginFactory socialLoginFactory;

    @BeforeEach
    void setUp() {
        lenient().when(googleService.getSupportedType()).thenReturn(SocialLoginType.GOOGLE);
        lenient().when(kakaoService.getSupportedType()).thenReturn(SocialLoginType.KAKAO);
        lenient().when(naverService.getSupportedType()).thenReturn(SocialLoginType.NAVER);

        List<SocialLoginService> services = Arrays.asList(googleService, kakaoService, naverService);
        socialLoginFactory = new SocialLoginFactory(services);
    }
    @Test
    @DisplayName("Google 소셜 로그인 서비스 조회")
    void getService_Google() {
        // when
        SocialLoginService result = socialLoginFactory.getService(SocialLoginType.GOOGLE);

        // then
        assertThat(result).isEqualTo(googleService);
    }

    @Test
    @DisplayName("Kakao 소셜 로그인 서비스 조회")
    void getService_Kakao() {
        // when
        SocialLoginService result = socialLoginFactory.getService(SocialLoginType.KAKAO);

        // then
        assertThat(result).isEqualTo(kakaoService);
    }

    @Test
    @DisplayName("Naver 소셜 로그인 서비스 조회")
    void getService_Naver() {
        // when
        SocialLoginService result = socialLoginFactory.getService(SocialLoginType.NAVER);

        // then
        assertThat(result).isEqualTo(naverService);
    }

    @Test
    @DisplayName("지원하지 않는 소셜 로그인 타입으로 조회 시 예외 발생")
    void getService_UnsupportedType() {
        // given
        SocialLoginFactory emptyFactory = new SocialLoginFactory(List.of());

        // when & then
        assertThatThrownBy(() -> emptyFactory.getService(SocialLoginType.GOOGLE))
                .isInstanceOf(GeneralException.class);
    }
}
