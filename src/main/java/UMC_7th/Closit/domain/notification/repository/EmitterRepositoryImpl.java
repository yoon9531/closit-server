package UMC_7th.Closit.domain.notification.repository;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class EmitterRepositoryImpl implements EmitterRepository{

    // emitterId를 키로 해시 맵에 SseEmitter 저장
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
    // eventCacheId를 키로 해시 맵에 서버에서 발생한 이벤트 저장
    private final Map<String, Object> eventCache = new ConcurrentHashMap<>();

    // SseEmitter 저장
    @Override
    public SseEmitter save(String emitterId, SseEmitter sseEmitter) {
        emitters.put(emitterId, sseEmitter);
        return sseEmitter;
    }

    // SseEmitter 삭제
    @Override
    public void deleteById(String emitterId) {
        emitters.remove(emitterId);
    }

    // 사용자와 관련된 SseEmitter 탐색
    @Override
    public Map<String, SseEmitter> findAllEmitterByUserId(String userId) {
        return emitters.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(userId))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    // 사용자와 관련된 이벤트 탐색
    @Override
    public Map<String, Object> findAllEventCacheByUserId(String userId) {
        return eventCache.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(userId))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}