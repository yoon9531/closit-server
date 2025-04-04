package UMC_7th.Closit.domain.emailtoken.service;

public interface MailService {

    void sendEmail(String to, String subject, String text);
}
