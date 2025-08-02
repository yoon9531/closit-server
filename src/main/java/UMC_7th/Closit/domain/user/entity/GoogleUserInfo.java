package UMC_7th.Closit.domain.user.entity;


import UMC_7th.Closit.global.common.SocialLoginType;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import lombok.RequiredArgsConstructor;

public class GoogleUserInfo implements OAuthUserInfo {
    private final GoogleIdToken.Payload payload;
    private final JsonNode userInfoJson;

    // ID Token용 생성자
    public GoogleUserInfo(GoogleIdToken.Payload payload) {
        this.payload = payload;
        this.userInfoJson = null;
    }

    // Access Token용 생성자
    public GoogleUserInfo(JsonNode userInfoJson) {
        this.payload = null;
        this.userInfoJson = userInfoJson;
    }

    @Override
    public String providerId() {
        if (payload != null) {
            return payload.getSubject();
        }
        return userInfoJson.get("id").asText();
    }

    @Override
    public String getEmail() {
        if (payload != null) {
            return payload.getEmail();
        }
        return userInfoJson.get("email").asText();
    }

    @Override
    public String getName() {
        if (payload != null) {
            return payload.get("name").toString();
        }
        return userInfoJson.get("name").asText();
    }

    @Override
    public SocialLoginType getProvider() {
        return SocialLoginType.GOOGLE;
    }
}
