package UMC_7th.Closit.domain.notification.firebase.converter;

import UMC_7th.Closit.domain.notification.firebase.domain.FcmToken;
import UMC_7th.Closit.domain.notification.firebase.dto.FcmTokenRequest;
import UMC_7th.Closit.domain.user.entity.User;

public class FcmTokenConverter {

    public static FcmToken toEntity(FcmTokenRequest request, User user) {
        return FcmToken.builder()
                .token(request.token())
                .user(user)
                .build();
    }
}
