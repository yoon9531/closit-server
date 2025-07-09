package UMC_7th.Closit.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenBlackListServiceImpl implements TokenBlackListService {

    private final TokenBlackListRedisService tokenBlackListRedisService;

    private static final long EXPIRATION_HOURS = 7L;

    @Override
    public void blacklistToken(String accessToken, String email) {
        tokenBlackListRedisService.save(accessToken, email, EXPIRATION_HOURS);
    }

    @Override
    public boolean isTokenBlacklisted(String accessToken) {
        return tokenBlackListRedisService.exists(accessToken);
    }

    @Override
    public void removeToken(String accessToken) {
        tokenBlackListRedisService.delete(accessToken);
    }
}
