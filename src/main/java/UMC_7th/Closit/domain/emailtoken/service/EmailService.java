package UMC_7th.Closit.domain.emailtoken.service;

public interface EmailService {

    void sendEmail(String to, String subject, String text);
}
