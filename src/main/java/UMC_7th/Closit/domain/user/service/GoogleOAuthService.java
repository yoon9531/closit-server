package UMC_7th.Closit.domain.user.service;

import UMC_7th.Closit.domain.user.entity.GoogleUserInfo;
import UMC_7th.Closit.domain.user.entity.OAuthUserInfo;
import UMC_7th.Closit.global.apiPayload.code.status.ErrorStatus;
import UMC_7th.Closit.global.apiPayload.exception.handler.UserHandler;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;


@Service
@RequiredArgsConstructor
public class GoogleOAuthService {

    @Value("${oauth.google.client-id}")
    private String googleClientId;

    private static final JsonFactory jsonFactory = new GsonFactory();

    public OAuthUserInfo getUserInfo(String idTokenString) {
        try {

            // Google IdToken 검증
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    new NetHttpTransport(), jsonFactory)
                    .setAudience(Collections.singletonList(googleClientId))
                    .build();

            GoogleIdToken idToken = verifier.verify(idTokenString);

            if (idToken == null) {
                throw new UserHandler(ErrorStatus.INVALID_TOKEN);
            }

            return new GoogleUserInfo(idToken.getPayload());

        } catch (Exception e) {
            throw new UserHandler(ErrorStatus.INVALID_TOKEN);
        }
    }
}
