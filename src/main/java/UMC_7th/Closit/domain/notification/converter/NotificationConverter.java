package UMC_7th.Closit.domain.notification.converter;

import UMC_7th.Closit.domain.notification.dto.NotificationRequestDTO;
import UMC_7th.Closit.domain.notification.dto.NotificationResponseDTO;
import UMC_7th.Closit.domain.notification.entity.Notification;
import UMC_7th.Closit.domain.notification.entity.NotificationType;
import UMC_7th.Closit.domain.user.entity.User;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.stream.Collectors;

public class NotificationConverter {

    public static Notification toNotification (User user, NotificationRequestDTO.SendNotiRequestDTO request) { // 알림 전송
        return Notification.builder()
                .user(user)
                .content(request.getContent())
                .type(request.getType())
                .build();
    }

    public static NotificationResponseDTO.SendNotiResultDTO sendNotiResult (Notification notification) {
        return NotificationResponseDTO.SendNotiResultDTO.builder()
                .notificationId(notification.getId())
                .content(notification.getContent())
                .type(notification.getType())
                .isRead(notification.isRead())
                .createdAt(notification.getCreatedAt())
                .build();
    }

    public static NotificationRequestDTO.SendNotiRequestDTO sendNotiRequest (User receiver, String content, NotificationType notificationType) {
        return NotificationRequestDTO.SendNotiRequestDTO.builder()
                .receiverId(receiver.getId())
                .content(content)
                .type(notificationType)
                .build();
    }

    public static NotificationResponseDTO.NotiPreviewDTO notiPreviewDTO (Notification notification) { // 알림 단건 조회
        return NotificationResponseDTO.NotiPreviewDTO.builder()
                .notificationId(notification.getId())
                .clositId(notification.getUser().getClositId())
                .userName(notification.getUser().getName())
                .imageUrl(notification.getSender().getProfileImage())
                .content(notification.getContent())
                .type(notification.getType())
                .isRead(notification.isRead())
                .build();
    }

    public static NotificationResponseDTO.NotiPreviewListDTO notiPreviewListDTO (Slice<Notification> notificationList) { // 알림 목록 조회
        List<NotificationResponseDTO.NotiPreviewDTO> notiPreviewDTOList = notificationList.stream()
                .map(NotificationConverter::notiPreviewDTO).collect(Collectors.toList());

        return NotificationResponseDTO.NotiPreviewListDTO.builder()
                .notiPreviewDTOList(notiPreviewDTOList)
                .listSize(notiPreviewDTOList.size())
                .isFirst(notificationList.isFirst())
                .isLast(notificationList.isLast())
                .hasNext(notificationList.hasNext())
                .build();
    }
}