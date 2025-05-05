package UMC_7th.Closit.global.util;

import UMC_7th.Closit.global.apiPayload.code.status.ErrorStatus;
import UMC_7th.Closit.global.apiPayload.exception.handler.EmailTemplateLoadHandler;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class HtmlTemplateUtil {

    public String loadVerificationEmailTemplate(String verificationLink) {
        try {
            Path path = new ClassPathResource("templates/email/verification-template.html").getFile().toPath();
            String template = Files.readString(path);
            return template.replace("{{VERIFICATION_LINK}}", verificationLink);
        } catch (IOException e) {
            throw new EmailTemplateLoadHandler(ErrorStatus.EMAIL_TEMPLATE_LOAD_FAILED);
        }
    }

    public static String loadHtml(String filename) {
        try {
            ClassPathResource resource = new ClassPathResource("static/" + filename);
            Path path = resource.getFile().toPath();
            return Files.readString(path);
        } catch (IOException e) {
            return "<h1>페이지 로드 실패</h1>";
        }
    }
}
