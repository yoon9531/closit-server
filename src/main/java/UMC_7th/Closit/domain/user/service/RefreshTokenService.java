package UMC_7th.Closit.domain.user.service;

import UMC_7th.Closit.domain.user.entity.RefreshToken;
import org.springframework.stereotype.Service;

import java.sql.Ref;
import java.util.Optional;

public interface RefreshTokenService {
    RefreshToken saveRefreshToken(String username, String refreshToken);
    RefreshToken findRefreshTokenByUsername(String username);
    void deleteRefreshToken(String username);
}
