package UMC_7th.Closit.domain.emailtoken.service;

import UMC_7th.Closit.domain.emailtoken.entity.EmailToken;
import UMC_7th.Closit.domain.emailtoken.repository.EmailTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailTokenServiceImpl implements EmailTokenService {

    private final EmailTokenRepository emailTokenRepository;
    private final MailService mailService; // 이메일 보내는 서비스

    @Override
    public void createEmailToken(String email) {
        String token = UUID.randomUUID().toString();
        LocalDateTime expiredAt = LocalDateTime.now().plusMinutes(30);

        EmailToken emailToken = EmailToken.builder()
                .email(email)
                .token(token)
                .expiredAt(expiredAt)
                .used(false)
                .build();

        emailTokenRepository.save(emailToken);

        // 이메일 발송
        String verificationLink = "https://yourdomain.com/api/auth/verify-email?token=" + token;
        mailService.sendEmail(email, "이메일 인증", verificationLink);
    }

    @Override
    public void verifyEmailToken(String token) {
        EmailToken emailToken = emailTokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("잘못된 토큰입니다."));

        if (emailToken.isUsed()) {
            throw new IllegalStateException("이미 사용된 토큰입니다.");
        }

        if (emailToken.getExpiredAt().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("토큰이 만료되었습니다.");
        }

        emailToken.setUsed(true);
        emailTokenRepository.save(emailToken);
    }
}
