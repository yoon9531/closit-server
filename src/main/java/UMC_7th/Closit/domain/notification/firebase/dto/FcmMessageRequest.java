package UMC_7th.Closit.domain.notification.firebase.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record FcmMessageRequest(
        @Schema(description = "알림 메시지 제목", example = "Closit")
        String title,

        @Schema(description = "알림 메시지 내용", example = "[nnrmm] 배틀을 신청했습니다.")
        String body
) {
}
