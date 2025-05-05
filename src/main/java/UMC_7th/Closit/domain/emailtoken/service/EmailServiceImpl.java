package UMC_7th.Closit.domain.emailtoken.service;

import UMC_7th.Closit.global.apiPayload.code.status.ErrorStatus;
import UMC_7th.Closit.global.apiPayload.exception.handler.EmailEncodingHandler;
import UMC_7th.Closit.global.apiPayload.exception.handler.EmailSendHandler;
import UMC_7th.Closit.global.util.HtmlTemplateUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final HtmlTemplateUtil htmlTemplateUtil;

    @Override
    public void sendEmail(String to, String subject, String verificationLink) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            try {
                helper.setFrom("clositback@gmail.com", "Closit");
            } catch (UnsupportedEncodingException e) {
                throw new EmailEncodingHandler(ErrorStatus.EMAIL_SENDER_ENCODING_ERROR);
            }

            String htmlContent = htmlTemplateUtil.loadVerificationEmailTemplate(verificationLink);
            helper.setText(htmlContent, true); // HTML 본문 설정 (true)

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new EmailSendHandler(ErrorStatus.EMAIL_SEND_FAILED);
        }
    }
}
