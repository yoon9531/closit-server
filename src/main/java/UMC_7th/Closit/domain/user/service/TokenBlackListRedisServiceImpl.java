package UMC_7th.Closit.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class TokenBlackListRedisServiceImpl implements TokenBlackListRedisService {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String PREFIX = "blacklistToken:";

    @Override
    public void save(String accessToken, String clositId, long expirationHours) {
        String key = PREFIX + accessToken;
        redisTemplate.opsForValue().set(key, clositId, Duration.ofHours(expirationHours));
    }

    @Override
    public boolean exists(String accessToken) {
        return redisTemplate.hasKey(PREFIX + accessToken);
    }

    @Override
    public void delete(String accessToken) {
        redisTemplate.delete(PREFIX + accessToken);
    }
}
