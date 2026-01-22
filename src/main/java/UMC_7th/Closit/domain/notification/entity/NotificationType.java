package UMC_7th.Closit.domain.notification.entity;

import io.swagger.v3.oas.annotations.media.Schema;

public enum NotificationType {
    @Schema(description = "좋아요 알림")
    LIKE,
    @Schema(description = "댓글 알림")
    COMMENT,
    @Schema(description = "팔로우 알림")
    FOLLOW,
    @Schema(description = "미션 알림")
    MISSION,
    @Schema(description = "배틀 신청 알림")
    CHALLENGE_BATTLE
}