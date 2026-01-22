package UMC_7th.Closit.domain.notification.repository;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;

@Repository
public interface EmitterRepository {
    SseEmitter save(String emitterId, SseEmitter sseEmitter); // SseEmitter 저장
    void deleteById(String emitterId); // SseEmitter 삭제
    Map<String, SseEmitter> findAllEmitterByUserId(String userId); // 사용자와 관련된 SseEmitter 탐색
    Map<String, Object> findAllEventCacheByUserId(String userId); // 사용자와 관련된 이벤트 탐색
}