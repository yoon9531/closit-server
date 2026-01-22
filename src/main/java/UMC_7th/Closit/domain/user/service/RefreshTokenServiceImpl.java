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
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public RefreshToken saveRefreshToken(String username, String refreshToken) {
        RefreshToken token = RefreshToken.builder()
                .username(username)
                .refreshToken(refreshToken)
                .build();

        return refreshTokenRepository.save(token);
    }

    @Override
    public RefreshToken findRefreshTokenByUsername(String username) {

        return refreshTokenRepository.findByUsername(username)
                .orElseThrow(()-> new UserHandler(ErrorStatus.USER_NOT_FOUND));
    }

    @Override
    public void deleteRefreshToken(String username) {

        refreshTokenRepository.deleteByUsername(username);
    }
}
