package UMC_7th.Closit.domain.emailtoken.service;

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

    private final EmailTokenRedisService emailTokenRedisService;
    private final EmailService emailService;

    @Value("${server.domain}")
    private String serverDomain;

    private static final long EXPIRATION_MINUTES = 30;

    @Override
    public void createEmailToken(String email) {
        String token = UUID.randomUUID().toString();
        LocalDateTime expiredAt = LocalDateTime.now().plusMinutes(30);

//        EmailToken emailToken = EmailToken.builder()
//                .email(email)
//                .token(token)
//                .expiredAt(expiredAt)
//                .verified(false)
//                .used(false)
//                .build();
//
//        emailTokenRepository.save(emailToken);

        // Redis에 email → token 저장 (TTL 30분)
        emailTokenRedisService.saveToken(email, token, EXPIRATION_MINUTES);
        emailTokenRedisService.saveTokenReverse(token, email, EXPIRATION_MINUTES);

        // 이메일 발송
        String verificationLink = serverDomain + "/api/v1/email-tokens/verify?token=" + token;
        emailService.sendEmail(email, "[Closit] 이메일 인증을 완료해주세요", verificationLink);
    }

    @Override
    public void verifyEmailToken(String token) {
//        EmailToken emailToken = emailTokenRepository.findByToken(token)
//                .orElseThrow(() -> new EmailTokenHandler(ErrorStatus.EMAIL_TOKEN_NOT_FOUND));
//
//        if (emailToken.isVerified()) {
//            throw new EmailTokenHandler(ErrorStatus.EMAIL_TOKEN_ALREADY_VERIFIED);
//        }
//
//        emailToken.validateUsable();
//
//        emailToken.verify();
//        emailTokenRepository.save(emailToken);

        String email = emailTokenRedisService.getEmailByToken(token);

        if (email == null) {
            throw new EmailTokenHandler(ErrorStatus.EMAIL_TOKEN_NOT_FOUND);
        }

        if (emailTokenRedisService.isVerified(email)) {
            throw new EmailTokenHandler(ErrorStatus.EMAIL_TOKEN_ALREADY_VERIFIED);
        }

        // 인증 완료 플래그 저장
        emailTokenRedisService.markVerified(email);

        // 토큰 제거 (옵션)
        emailTokenRedisService.deleteToken(email);
        emailTokenRedisService.deleteTokenReverse(token);
    }

    @Override
    public boolean isEmailVerified(String email) {
//        return emailTokenRepository.findTopByEmailOrderByCreatedAtDesc(email)
//                .map(token -> token.isVerified() && !token.isUsed() && token.getExpiredAt().isAfter(LocalDateTime.now()))
//                .orElse(false);

        return emailTokenRedisService.isVerified(email);
    }

    @Override
    public void markTokenAsUsed(String email) {
//        EmailToken token = emailTokenRepository.findTopByEmailOrderByCreatedAtDesc(email)
//                .orElseThrow(() -> new EmailTokenHandler(ErrorStatus.EMAIL_TOKEN_NOT_FOUND));
//
//        if (!token.isVerified()) {
//            throw new EmailTokenHandler(ErrorStatus.EMAIL_NOT_VERIFIED);
//        }
//
//        token.validateUsable();
//
//        token.use();
//        emailTokenRepository.save(token);

        if (!emailTokenRedisService.isVerified(email)) {
            throw new EmailTokenHandler(ErrorStatus.EMAIL_NOT_VERIFIED);
        }

        // 인증 완료된 이메일이면 토큰 무효화 처리
        emailTokenRedisService.markUsed(email);
    }
}
