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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoogleOAuthService {

    @Value("${oauth.google.client-id}")
    private String googleClientId;

    private static final JsonFactory jsonFactory = new GsonFactory();

    public OAuthUserInfo getUserInfo(String idTokenString) {
        try {

            // Verifier Object 생성
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    new NetHttpTransport(), jsonFactory)
                    .setAudience(Collections.singletonList(googleClientId))
                    .build();

            GoogleIdToken idToken = verifier.verify(idTokenString);

            if (idToken == null) {
                log.info("Invalid ID token.", idTokenString);
                throw new UserHandler(ErrorStatus.INVALID_TOKEN);
            }

            GoogleIdToken.Payload payload = idToken.getPayload();

            // Issuer 체크
            if (!"accounts.google.com".equals(payload.getIssuer()) && !"https://accounts.google.com".equals(payload.getIssuer())) {
                log.info("Check Issuer");
                throw new UserHandler(ErrorStatus.INVALID_TOKEN);
            }

            // aud (Audience) 검증
            if (!googleClientId.equals(payload.getAudience())) {
                log.info("Check Audience");
                throw new UserHandler(ErrorStatus.INVALID_TOKEN);
            }

            return new GoogleUserInfo(payload);
        } catch (Exception e) {
            throw new UserHandler(ErrorStatus.INVALID_TOKEN);
        }
    }
}
