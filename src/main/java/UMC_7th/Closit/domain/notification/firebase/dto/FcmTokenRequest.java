package UMC_7th.Closit.domain.notification.firebase.dto;

import lombok.Builder;

@Builder
public record FcmTokenRequest(
        String token
) {
}
