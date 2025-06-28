package UMC_7th.Closit.domain.notification.repository;

import UMC_7th.Closit.domain.notification.entity.Notification;
import UMC_7th.Closit.domain.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Slice<Notification> findByUserAndIsReadFalseOrderByCreatedAtDesc(User user, Pageable pageable); // 알림 목록 조회 - isRead = false
    Slice<Notification> findByUserAndIsReadTrueOrderByUpdatedAtDesc(User user, Pageable pageable); // 알림 목록 조회 - isRead = true

    @Modifying
    @Query("UPDATE Notification n SET n.isRead = true, n.updatedAt = :updatedAt WHERE n.user.id = :userId AND n.isRead = false")
    void updateReadStatusByUserId (@Param("userId") Long userId, @Param("updatedAt") LocalDateTime updatedAt); // isRead 업데이트
}