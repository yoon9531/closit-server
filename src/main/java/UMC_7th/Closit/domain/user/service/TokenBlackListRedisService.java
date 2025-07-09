package UMC_7th.Closit.domain.user.service;

public interface TokenBlackListRedisService {

    void save(String accessToken, String email, long expirationHours);

    boolean exists(String accessToken);

    void delete(String accessToken);
}
