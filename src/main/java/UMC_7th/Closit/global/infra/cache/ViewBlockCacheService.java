package UMC_7th.Closit.global.infra.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;

@Service
public class ViewBlockCacheService {
    private final Cache<String, Boolean> viewCache = Caffeine.newBuilder()
            .expireAfterWrite(1, TimeUnit.HOURS)  // 1시간 동안 유지
            .maximumSize(100_000)                // 최대 캐시 크기 설정
            .build();

    public boolean shouldIncreaseView(Long userId, Long postId) {
        String key = generateKey(userId, postId);
        if (viewCache.getIfPresent(key) != null) {
            return false; // 이미 본 적 있음 → 증가 안 함
        }
        viewCache.put(key, true);
        return true; // 처음 본 경우 → 증가 가능
    }

    private String generateKey(Long userId, Long postId) {
        return userId + ":" + postId;
    }
}
