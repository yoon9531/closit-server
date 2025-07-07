package UMC_7th.Closit.domain.user.service;

public interface RefreshTokenRedisService {

    void save(String username, String refreshToken, long expirationDays);

    String get(String username);

    void delete(String username);
}
