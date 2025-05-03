package UMC_7th.Closit.domain.emailtoken.service;

public interface EmailTokenService {

    void createEmailToken(String email);

    void verifyEmailToken(String token);

    boolean isEmailVerified(String email);

    void markTokenAsUsed(String email);
}
