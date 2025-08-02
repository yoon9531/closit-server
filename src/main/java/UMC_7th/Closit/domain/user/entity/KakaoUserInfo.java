package UMC_7th.Closit.domain.user.entity;

import UMC_7th.Closit.global.common.SocialLoginType;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class KakaoUserInfo implements OAuthUserInfo {

    private final JsonNode userInfoJson;

    @Override
    public String providerId() {
        return userInfoJson.get("id").asText();
    }

    @Override
    public String getEmail() {
        JsonNode kakaoAccount = userInfoJson.get("kakao_account");
        if (kakaoAccount != null && kakaoAccount.has("email")) {
            return kakaoAccount.get("email").asText();
        }
        throw new IllegalStateException("카카오 계정에서 이메일 정보를 가져올 수 없습니다.");
    }

    @Override
    public String getName() {
        JsonNode properties = userInfoJson.get("properties");
        if (properties != null && properties.has("nickname")) {
            return properties.get("nickname").asText();
        }

        JsonNode kakaoAccount = userInfoJson.get("kakao_account");
        if (kakaoAccount != null && kakaoAccount.has("profile")) {
            JsonNode profile = kakaoAccount.get("profile");
            if (profile.has("nickname")) {
                return profile.get("nickname").asText();
            }
        }

        return "카카오 사용자";
    }

    @Override
    public SocialLoginType getProvider() {
        return SocialLoginType.KAKAO;
    }
}

