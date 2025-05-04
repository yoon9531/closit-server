package UMC_7th.Closit.domain.notification.service;

import UMC_7th.Closit.domain.battle.entity.ChallengeBattle;
import UMC_7th.Closit.domain.follow.entity.Follow;
import UMC_7th.Closit.domain.notification.converter.NotificationConverter;
import UMC_7th.Closit.domain.notification.dto.NotificationRequestDTO;
import UMC_7th.Closit.domain.notification.entity.Notification;
import UMC_7th.Closit.domain.notification.entity.NotificationType;
import UMC_7th.Closit.domain.notification.repository.NotificationRepository;
import UMC_7th.Closit.domain.notification.repository.EmitterRepository;
import UMC_7th.Closit.domain.post.entity.Comment;
import UMC_7th.Closit.domain.post.entity.Likes;
import UMC_7th.Closit.domain.post.entity.Post;
import UMC_7th.Closit.domain.post.repository.PostRepository;
import UMC_7th.Closit.domain.user.entity.User;
import UMC_7th.Closit.domain.user.repository.UserRepository;
import UMC_7th.Closit.global.apiPayload.code.status.ErrorStatus;
import UMC_7th.Closit.global.apiPayload.exception.GeneralException;
import UMC_7th.Closit.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Transactional
public class NotiCommandServiceImpl implements NotiCommandService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final NotificationRepository notificationRepository;
    private final EmitterRepository emitterRepository;
    private final SecurityUtil securityUtil;

    @Value("${notification.url}")
    private String URL;

    @Override
    public SseEmitter subscribe(Long userId, String lastEventId) { // SSE 연결
        // 연결 지속 시간 = 1시간
        SseEmitter emitter = new SseEmitter(60 * 60 * 1000L);
        String emitterId = userId + "-" + System.currentTimeMillis();

        emitterRepository.save(emitterId, emitter);

        // 완료, 시간초과로 인한 전송 불가 -> SseEmitter 삭제
        emitter.onCompletion(() -> emitterRepository.deleteById(emitterId));
        emitter.onTimeout(() -> emitterRepository.deleteById(emitterId));

        // 연결 성공 메시지 전송
        sendToClient(emitter, emitterId, "SSE Connection Complete, EventStream Created. [userId=" + userId + "]");

        if (!lastEventId.isEmpty()) { // Last-Event-ID 존재 = 받지 못한 데이터 존재 (클라이언트가 놓친 이벤트 존재)
            Map<String, Object> events = emitterRepository.findAllEventCacheByUserId(emitterId);
            events.entrySet().stream()
                    .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0) // 클라이언트가 놓친 이벤트 필터링 -> lastEventId 이후 발생한 이벤트만 통과
                    .forEach(entry -> sendToClient(emitter, entry.getKey(), entry.getValue()));
        }
        // 미션 알림
        missionNotification(emitter, emitterId, "MISSION");

        return emitter;
    }

    // SseEmitter = 최초 연결 시 생성, 해당 SseEmitter를 생성한 클라이언트에게 알림 전송
    @Override
    public void sendToClient(SseEmitter emitter, String emitterId, Object data) { // 클라이언트 알림 전송
        try {
            emitter.send(SseEmitter.event()
                    .id(emitterId)
                    .name("SSE")
                    .data(data));
        } catch (IOException exception) {
            emitterRepository.deleteById(emitterId);
            throw new GeneralException(ErrorStatus.SSE_CONNECT_FAILED);
        }
    }

    @Override
    public Notification sendNotification(NotificationRequestDTO.SendNotiRequestDTO request) { // 알림 전송
        User user = userRepository.findById(request.getReceiverId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        User sender = userRepository.findById(request.getSenderId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        Notification notification = NotificationConverter.toNotification(user, sender, request);

        // 저장 후 전송
        notificationRepository.save(notification);

        // SSE 통한 알림 전송
        Map<String, SseEmitter> emitters = emitterRepository.findAllEmitterByUserId(request.getReceiverId().toString());

        emitters.forEach(
                (key, emitter) -> {
                    // 데이터 전송
                    sendToClient(emitter, key, NotificationConverter.sendNotiResponse(notification));
                }
        );
        return notification;
    }

    @Override
    public void likeNotification(Likes likes) { // 좋아요 알림
        User receiver = likes.getPost().getUser(); // 게시글 작성자
        User sender = likes.getUser();
        String content = likes.getUser().getName() + "님이 좋아요를 눌렀습니다.";
        String url = URL + "/posts/" + likes.getPost().getId() + "/likes";

        // 좋아요를 누른 사용자가 본인이 아닐 경우, 알림 전송
        if (!receiver.getId().equals(likes.getUser().getId())) {
            NotificationRequestDTO.SendNotiRequestDTO request = NotificationConverter.sendNotiRequest(receiver, sender, content, url, NotificationType.LIKE);
            sendNotification(request);
        }
    }

    @Override
    public void commentNotification(Comment comment) { // 댓글 알림
        User receiver = comment.getPost().getUser(); // 게시글 작성자 ID
        User sender = comment.getUser();
        String content = comment.getUser().getName() + "님이 댓글을 작성했습니다.";
        String url = URL + "/posts/" + comment.getPost().getId() + "/comments";

        // 댓글을 작성한 사용자가 본인이 아닐 경우, 알림 전송
        if (!receiver.getId().equals(comment.getUser().getId())) {
            NotificationRequestDTO.SendNotiRequestDTO request = NotificationConverter.sendNotiRequest(receiver, sender, content, url, NotificationType.COMMENT);
            sendNotification(request);
        }
    }

    @Override
    public void followNotification(Follow follow) { // 팔로우 알림
        User receiver = follow.getReceiver(); // 팔로워 알림 받는 사용자
        User sender = follow.getSender();
        String content = follow.getSender().getName() + "님이 회원님을 팔로우하기 시작했습니다.";
        String url = URL + "/users/" + sender.getClositId();

        NotificationRequestDTO.SendNotiRequestDTO request = NotificationConverter.sendNotiRequest(receiver, sender, content, url, NotificationType.FOLLOW);

        sendNotification(request);
    }

    @Override
    public void missionNotification(SseEmitter emitter, String emitterId, Object data) { // 미션 알림
        User receiver = securityUtil.getCurrentUser(); // 현재 로그인한 사용자
        String content = receiver.getName() + "님 오늘의 미션을 수행해주세요!";

        List<Post> posts = postRepository.findByUserId(receiver.getId());

        // 하루 이내에 사용자가 올린 게시물이 있는지 확인
        LocalDateTime today = LocalDateTime.now().with(LocalTime.MIDNIGHT);
        boolean existsPost = posts.stream()
                .anyMatch(post -> post.getCreatedAt().isAfter(today));

        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.schedule(() -> {
            // 하루 이내 사용자가 올린 게시글이 없을 경우, 알림 전송
            if (!existsPost) {
                sendToClient(emitter, emitterId, content.getBytes(StandardCharsets.UTF_8));
            } scheduler.shutdown(); // 작업 완료 후, 스레드 종료
        }, 10, TimeUnit.SECONDS); // SSE 연결 10초 후, 한 번만 실행
    }

    @Override
    public void challengeBattleNotification(ChallengeBattle challengeBattle) {
        User receiver = challengeBattle.getBattle().getPost1().getUser();
        User sender = challengeBattle.getPost().getUser();
        String content = challengeBattle.getPost().getUser().getName() + "님이 배틀을 신청하셨습니다.";
        String url = URL + "/communities/battle/challenge/upload/" + challengeBattle.getBattle().getId();

        NotificationRequestDTO.SendNotiRequestDTO request = NotificationConverter.sendNotiRequest(receiver, sender, content, url, NotificationType.CHALLENGE_BATTLE);

        sendNotification(request);
    }

    @Override
    public void deleteNotification(Long userId, Long notificationId) { // 알림 삭제
        notificationRepository.findById(notificationId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.NOTIFICATION_NOT_FOUND));

        notificationRepository.deleteById(notificationId);
    }
}