package UMC_7th.Closit.domain.user.service;

public interface RefreshTokenService {

    void saveRefreshToken(String email, String refreshToken);

    String findRefreshTokenByEmail(String email);

    void deleteRefreshToken(String email);
}
