package UMC_7th.Closit.domain.user.service.social;

import UMC_7th.Closit.domain.user.entity.OAuthUserInfo;
import UMC_7th.Closit.global.common.SocialLoginType;

public interface SocialLoginService {

    /**
     * 지원하는 소셜 로그인 타입 반환
     */
    SocialLoginType getSupportedType();

    /**
     * ID Token을 검증하고 사용자 정보를 반환
     * @param idToken 소셜 플랫폼에서 발급받은 ID Token
     * @return 검증된 사용자 정보
     */
    OAuthUserInfo getUserInfoFromIdToken(String idToken);

    /**
     * Access Token을 검증하고 사용자 정보를 반환
     * @param accessToken 소셜 플랫폼에서 발급받은 Access Token
     * @return 검증된 사용자 정보
     */
    OAuthUserInfo getUserInfoFromAccessToken(String accessToken);
}

