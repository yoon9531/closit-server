package UMC_7th.Closit.domain.user.entity;

import UMC_7th.Closit.global.common.SocialLoginType;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class NaverUserInfo implements OAuthUserInfo {

    private final JsonNode userInfoJson;

    @Override
    public String providerId() {
        JsonNode response = userInfoJson.get("response");
        if (response != null && response.has("id")) {
            return response.get("id").asText();
        }
        throw new IllegalStateException("네이버 응답에서 사용자 ID를 가져올 수 없습니다.");
    }

    @Override
    public String getEmail() {
        JsonNode response = userInfoJson.get("response");
        if (response != null && response.has("email")) {
            return response.get("email").asText();
        }
        throw new IllegalStateException("네이버 계정에서 이메일 정보를 가져올 수 없습니다.");
    }

    @Override
    public String getName() {
        JsonNode response = userInfoJson.get("response");
        if (response != null) {
            if (response.has("name")) {
                return response.get("name").asText();
            }
            if (response.has("nickname")) {
                return response.get("nickname").asText();
            }
        }
        return "네이버 사용자";
    }

    @Override
    public SocialLoginType getProvider() {
        return SocialLoginType.NAVER;
    }
}

