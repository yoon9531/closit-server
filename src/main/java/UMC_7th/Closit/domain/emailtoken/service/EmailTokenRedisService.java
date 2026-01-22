package UMC_7th.Closit.domain.emailtoken.service;

public interface EmailTokenRedisService {

    void saveToken(String email, String token, long expirationMinutes);
    String getToken(String email);
    void deleteToken(String email);

    void saveTokenReverse(String token, String email, long expirationMinutes);
    String getEmailByToken(String token);
    void deleteTokenReverse(String token);

    void markVerified(String email);
    boolean isVerified(String email);

    void markUsed(String email);
    boolean isUsed(String email);
}
