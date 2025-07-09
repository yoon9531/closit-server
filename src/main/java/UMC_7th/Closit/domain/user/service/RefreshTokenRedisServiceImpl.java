package UMC_7th.Closit.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RefreshTokenRedisServiceImpl implements RefreshTokenRedisService {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String PREFIX = "refreshToken:";

    @Override
    public void save(String email, String refreshToken, long expirationDays) {
        String key = PREFIX + email;
        redisTemplate.opsForValue().set(key, refreshToken, Duration.ofDays(expirationDays));
    }

    @Override
    public String get(String email) {
        return (String) redisTemplate.opsForValue().get(PREFIX + email);
    }

    @Override
    public void delete(String email) {
        redisTemplate.delete(PREFIX + email);
    }
}
