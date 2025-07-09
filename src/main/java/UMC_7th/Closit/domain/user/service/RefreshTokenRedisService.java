package UMC_7th.Closit.domain.user.service;

public interface RefreshTokenRedisService {

    void save(String email, String refreshToken, long expirationDays);

    String get(String email);

    void delete(String email);
}
