package UMC_7th.Closit.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    // private final RefreshTokenRepository refreshTokenRepository;
    private final RefreshTokenRedisService refreshTokenRedisService;

    private static final long EXPIRATION_DAYS = 14L;

    @Override
    public void saveRefreshToken(String email, String refreshToken) {
//        RefreshToken token = RefreshToken.builder()
//                .username(email)
//                .refreshToken(refreshToken)
//                .build();
//
//        return refreshTokenRepository.save(token);

        refreshTokenRedisService.save(email, refreshToken, EXPIRATION_DAYS);
    }

    @Override
    public String findRefreshTokenByEmail(String email) {
//        return refreshTokenRepository.findByUsername(email)
//                .orElseThrow(()-> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        String token = refreshTokenRedisService.get(email);

        if (token == null) {
            throw new IllegalStateException("해당 사용자의 리프레시 토큰이 존재하지 않습니다.");
        }

        return token;
    }

    @Override
    public void deleteRefreshToken(String email) {
//        refreshTokenRepository.deleteByUsername(email);

        refreshTokenRedisService.delete(email);
    }
}
