package UMC_7th.Closit.domain.notification.dto;

import UMC_7th.Closit.domain.notification.entity.NotificationType;
import lombok.Builder;
import lombok.Getter;

public class NotificationRequestDTO {

    @Getter
    @Builder
    public static class SendNotiRequestDTO { // 알림 전송
        private Long receiverId;
        private Long senderId;
        private String content;
        private NotificationType type;
    }
}