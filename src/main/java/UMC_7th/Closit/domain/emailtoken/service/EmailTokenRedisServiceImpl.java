package UMC_7th.Closit.domain.emailtoken.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class EmailTokenRedisServiceImpl implements EmailTokenRedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String PREFIX_TOKEN = "emailToken:";          // email → token
    private static final String PREFIX_TOKEN_REVERSE = "emailTokenReverse:"; // token → email
    private static final String PREFIX_VERIFIED = "emailVerified:";    // 인증 여부
    private static final String PREFIX_USED = "emailUsed:";            // 사용 여부

    @Override
    public void saveToken(String email, String token, long expirationMinutes) {
        redisTemplate.opsForValue().set(PREFIX_TOKEN + email, token, Duration.ofMinutes(expirationMinutes));
    }

    @Override
    public String getToken(String email) {
        return (String) redisTemplate.opsForValue().get(PREFIX_TOKEN + email);
    }

    @Override
    public void deleteToken(String email) {
        redisTemplate.delete(PREFIX_TOKEN + email);
    }

    @Override
    public void saveTokenReverse(String token, String email, long expirationMinutes) {
        redisTemplate.opsForValue().set(PREFIX_TOKEN_REVERSE + token, email, Duration.ofMinutes(expirationMinutes));
    }

    @Override
    public String getEmailByToken(String token) {
        return (String) redisTemplate.opsForValue().get(PREFIX_TOKEN_REVERSE + token);
    }

    @Override
    public void deleteTokenReverse(String token) {
        redisTemplate.delete(PREFIX_TOKEN_REVERSE + token);
    }

    @Override
    public void markVerified(String email) {
        redisTemplate.opsForValue().set(PREFIX_VERIFIED + email, "true");
    }

    @Override
    public boolean isVerified(String email) {
        String value = (String) redisTemplate.opsForValue().get(PREFIX_VERIFIED + email);
        return value != null && value.equals("true");
    }

    @Override
    public void markUsed(String email) {
        redisTemplate.opsForValue().set(PREFIX_USED + email, "true");
    }

    @Override
    public boolean isUsed(String email) {
        String value = (String) redisTemplate.opsForValue().get(PREFIX_USED + email);
        return value != null && value.equals("true");
    }
}
