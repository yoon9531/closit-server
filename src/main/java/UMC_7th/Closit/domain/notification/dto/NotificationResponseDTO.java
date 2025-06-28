package UMC_7th.Closit.domain.notification.dto;

import UMC_7th.Closit.domain.notification.entity.NotificationType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class NotificationResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SendNotiResultDTO { // 알림 전송
        private Long notificationId;
        private String content;
        private String url;
        private NotificationType type;
        private boolean isRead;
        @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
        private LocalDateTime createdAt;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NotiPreviewDTO { // 알림 단건 조회
        private Long notificationId;
        private String receiverClositId;
        private String imageUrl;
        private String content;
        private String url;
        private NotificationType type;
        private boolean isRead;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NotiPreviewListDTO { // 알림 목록 조회
        private List<NotiPreviewDTO> notiPreviewDTOList;
        private Integer listSize;
        private boolean isFirst;
        private boolean isLast;
        private boolean hasNext;
    }
}