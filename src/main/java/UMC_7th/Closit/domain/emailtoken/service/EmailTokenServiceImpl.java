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
    private final EmailService emailService;

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
        String verificationLink = "http://localhost:8080/api/auth/email-tokens/verify?token=" + token;
        emailService.sendEmail(email, "[Closit] 이메일 인증을 완료해주세요", verificationLink);
    }

    @Override
    public void verifyEmailToken(String token) {
        EmailToken emailToken = emailTokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("잘못된 토큰입니다."));

        if (emailToken.isVerified()) {
            throw new IllegalStateException("이미 인증된 토큰입니다.");
        }

        if (emailToken.isUsed()) {
            throw new IllegalStateException("이미 사용된 토큰입니다.");
        }

        if (emailToken.getExpiredAt().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("토큰이 만료되었습니다.");
        }

        emailToken.setVerified(true);
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
                .orElseThrow(() -> new IllegalArgumentException("인증된 토큰이 없습니다."));

        if (!token.isVerified() || token.isUsed() || token.getExpiredAt().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("유효한 인증 토큰이 아닙니다.");
        }

        token.setUsed(true);
        emailTokenRepository.save(token);
    }
}
