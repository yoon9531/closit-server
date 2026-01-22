package UMC_7th.Closit.domain.notification.service;

import UMC_7th.Closit.domain.notification.entity.Notification;
import UMC_7th.Closit.domain.notification.repository.NotificationRepository;
import UMC_7th.Closit.domain.user.entity.User;
import UMC_7th.Closit.domain.user.repository.UserRepository;
import UMC_7th.Closit.global.apiPayload.code.status.ErrorStatus;
import UMC_7th.Closit.global.apiPayload.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class NotiQueryServiceImpl implements NotiQueryService {

    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;

    @Override
    public Notification readNotification(Long userId, Long notificationId) { // 알림 단건 조회 - 읽음 처리
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.NOTIFICATION_NOT_FOUND));

        // 읽음 처리
        notification.markAsRead();

        return notificationRepository.save(notification);
    }

    @Override
    public Slice<Notification> getNotificationList(Long userId, Integer page) { // 알림 목록 조회 - 읽음 처리
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        Pageable pageable = PageRequest.of(page, 10);

        // new 알림 (isRead = false)
        Slice<Notification> newNotificationList = notificationRepository.findByUserAndIsReadFalseOrderByCreatedAtDesc(user, pageable);

        // last 알림 (isRead = true)
        Slice<Notification> lastNotificationList = notificationRepository.findByUserAndIsReadTrueOrderByUpdatedAtDesc(user, pageable);

        // new 알림 읽음 처리
        notificationRepository.updateReadStatusByUserId(userId, LocalDateTime.now());

        // new 알림 리스트 + last 알림 리스트
        List<Notification> notificationList = new ArrayList<>();
        notificationList.addAll(newNotificationList.getContent());
        notificationList.addAll(lastNotificationList.getContent());

        boolean hasNext = newNotificationList.hasNext() || lastNotificationList.hasNext();

        return new SliceImpl<>(notificationList, pageable, hasNext);
    }
}
