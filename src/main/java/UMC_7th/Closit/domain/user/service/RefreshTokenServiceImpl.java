package UMC_7th.Closit.domain.user.service;

import UMC_7th.Closit.domain.user.entity.RefreshToken;
import UMC_7th.Closit.domain.user.repository.RefreshTokenRepository;
import UMC_7th.Closit.global.apiPayload.code.status.ErrorStatus;
import UMC_7th.Closit.global.apiPayload.exception.handler.JwtHandler;
import UMC_7th.Closit.global.apiPayload.exception.handler.UserHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Ref;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    // private final RefreshTokenRepository refreshTokenRepository;
    private final RefreshTokenRedisService refreshTokenRedisService;

    private static final long EXPIRATION_DAYS = 14L;

    @Override
    public void saveRefreshToken(String username, String refreshToken) {
//        RefreshToken token = RefreshToken.builder()
//                .username(username)
//                .refreshToken(refreshToken)
//                .build();
//
//        return refreshTokenRepository.save(token);

        refreshTokenRedisService.save(username, refreshToken, EXPIRATION_DAYS);
    }

    @Override
    public String findRefreshTokenByUsername(String username) {
//        return refreshTokenRepository.findByUsername(username)
//                .orElseThrow(()-> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        String token = refreshTokenRedisService.get(username);

        if (token == null) {
            throw new IllegalStateException("해당 사용자의 리프레시 토큰이 존재하지 않습니다.");
        }

        return token;
    }

    @Override
    public void deleteRefreshToken(String username) {
//        refreshTokenRepository.deleteByUsername(username);

        refreshTokenRedisService.delete(username);
    }
}
