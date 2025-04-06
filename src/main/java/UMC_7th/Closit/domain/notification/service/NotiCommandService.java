package UMC_7th.Closit.domain.notification.service;

import UMC_7th.Closit.domain.battle.entity.ChallengeBattle;
import UMC_7th.Closit.domain.follow.entity.Follow;
import UMC_7th.Closit.domain.notification.dto.NotificationRequestDTO;
import UMC_7th.Closit.domain.notification.entity.Notification;
import UMC_7th.Closit.domain.post.entity.Comment;
import UMC_7th.Closit.domain.post.entity.Likes;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface NotiCommandService {
    SseEmitter subscribe (Long userId, String lastEventId); // SSE 연결
    void sendToClient (SseEmitter emitter, String emitterId, Object data); // 클라이언트 알림 전송
    Notification sendNotification (NotificationRequestDTO.SendNotiRequestDTO request); // SSE 알림 전송
    void commentNotification(Comment comment); // 댓글 알림
    void likeNotification(Likes likes); // 좋아요 알림
    void followNotification(Follow follow); // 팔로우 알림
    void missionNotification(SseEmitter emitter, String emitterId, Object data); // 미션 알림
    void challengeBattleNotification(ChallengeBattle challengeBattle); // 배틀 신청 알림
    void deleteNotification(Long userId, Long notificationId); // 알림 삭제
}