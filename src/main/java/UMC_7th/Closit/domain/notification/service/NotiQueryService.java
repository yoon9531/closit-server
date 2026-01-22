package UMC_7th.Closit.domain.notification.service;

import UMC_7th.Closit.domain.notification.entity.Notification;
import org.springframework.data.domain.Slice;

public interface NotiQueryService {
    Notification readNotification(Long userId, Long notificationId); // 알림 단건 조회 - 읽음 처리
    Slice<Notification> getNotificationList (Long userId, Integer page); // 알림 목록 조회 - 읽음 처리
}
