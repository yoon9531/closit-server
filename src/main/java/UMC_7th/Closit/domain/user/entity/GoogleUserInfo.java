package UMC_7th.Closit.domain.user.entity;


import UMC_7th.Closit.global.common.SocialLoginType;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GoogleUserInfo implements OAuthUserInfo {
    private final GoogleIdToken.Payload payload;

    @Override
    public String providerId () {
        return payload.getSubject();
    }

    @Override
    public String getEmail () {
        return payload.getEmail();
    }

    @Override
    public String getName () {
        return payload.get("name").toString();
    }

    @Override
    public SocialLoginType getProvider () {
        return SocialLoginType.GOOGLE;
    }
}
