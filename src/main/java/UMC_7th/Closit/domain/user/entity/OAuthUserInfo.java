package UMC_7th.Closit.domain.user.entity;

import UMC_7th.Closit.global.common.SocialLoginType;

public interface OAuthUserInfo {
    String providerId();
    String getEmail();
    String getName ();
    SocialLoginType getProvider();
}
