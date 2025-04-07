package UMC_7th.Closit.domain.emailtoken.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;

    @Override
    public void sendEmail(String to, String subject, String verificationLink) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);

            // HTML 메일 본문
            String htmlContent = "<div style=\"font-family: Arial, sans-serif; padding: 20px; text-align: center;\">" +
                    "<h2 style=\"color: #333;\">Closit 이메일 인증</h2>" +
                    "<p style=\"font-size: 16px;\">아래 버튼을 클릭해서 이메일 인증을 완료해주세요.</p>" +
                    "<a href=\"" + verificationLink + "\" " +
                    "style=\"display: inline-block; margin-top: 20px; padding: 10px 20px; background-color: #6C5CE7; color: #fff; text-decoration: none; border-radius: 5px;\">" +
                    "이메일 인증하기</a>" +
                    "<p style=\"margin-top: 20px; font-size: 12px; color: #888;\">버튼이 동작하지 않는다면 아래 링크를 복사해서 브라우저에 붙여넣어 주세요:</p>" +
                    "<p style=\"font-size: 12px; color: #888;\">" + verificationLink + "</p>" +
                    "</div>";

            helper.setText(htmlContent, true); // HTML 본문 설정 (true)

            mailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException("메일 전송 실패", e);
        }
    }
}
