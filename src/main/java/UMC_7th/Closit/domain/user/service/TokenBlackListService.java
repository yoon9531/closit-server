package UMC_7th.Closit.domain.user.service;

public interface TokenBlackListService {

    void blacklistToken(String accessToken, String email);

    boolean isTokenBlacklisted(String accessToken);

    void removeToken(String accessToken);
}
