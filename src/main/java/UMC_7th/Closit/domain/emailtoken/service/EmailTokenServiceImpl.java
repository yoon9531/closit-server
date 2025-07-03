package UMC_7th.Closit.domain.emailtoken.service;

import UMC_7th.Closit.domain.emailtoken.entity.EmailToken;
import UMC_7th.Closit.domain.emailtoken.repository.EmailTokenRepository;
import UMC_7th.Closit.global.apiPayload.code.status.ErrorStatus;
import UMC_7th.Closit.global.apiPayload.exception.handler.EmailTokenHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailTokenServiceImpl implements EmailTokenService {

    private final EmailTokenRepository emailTokenRepository;
    private final EmailService emailService;

    @Value("${server.domain}")
    private String serverDomain;

    @Override
    public void createEmailToken(String email) {
        String token = UUID.randomUUID().toString();
        LocalDateTime expiredAt = LocalDateTime.now().plusMinutes(30);

        EmailToken emailToken = EmailToken.builder()
                .email(email)
                .token(token)
                .expiredAt(expiredAt)
                .verified(false)
                .used(false)
                .build();

        emailTokenRepository.save(emailToken);

        // 이메일 발송
        String verificationLink = serverDomain + "/api/v1/email-tokens/verify?token=" + token;
        emailService.sendEmail(email, "[Closit] 이메일 인증을 완료해주세요", verificationLink);
    }

    @Override
    public void verifyEmailToken(String token) {
        EmailToken emailToken = emailTokenRepository.findByToken(token)
                .orElseThrow(() -> new EmailTokenHandler(ErrorStatus.EMAIL_TOKEN_NOT_FOUND));

        if (emailToken.isVerified()) {
            throw new EmailTokenHandler(ErrorStatus.EMAIL_TOKEN_ALREADY_VERIFIED);
        }

        emailToken.validateUsable();

        emailToken.verify();
        emailTokenRepository.save(emailToken);
    }

    @Override
    public boolean isEmailVerified(String email) {
        return emailTokenRepository.findTopByEmailOrderByCreatedAtDesc(email)
                .map(token -> token.isVerified() && !token.isUsed() && token.getExpiredAt().isAfter(LocalDateTime.now()))
                .orElse(false);
    }

    @Override
    public void markTokenAsUsed(String email) {
        EmailToken token = emailTokenRepository.findTopByEmailOrderByCreatedAtDesc(email)
                .orElseThrow(() -> new EmailTokenHandler(ErrorStatus.EMAIL_TOKEN_NOT_FOUND));

        if (!token.isVerified()) {
            throw new EmailTokenHandler(ErrorStatus.EMAIL_NOT_VERIFIED);
        }

        token.validateUsable();

        token.use();
        emailTokenRepository.save(token);
    }
}
