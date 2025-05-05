package UMC_7th.Closit.domain.emailtoken.entity;

import UMC_7th.Closit.global.apiPayload.code.status.ErrorStatus;
import UMC_7th.Closit.global.apiPayload.exception.handler.EmailTokenHandler;
import UMC_7th.Closit.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class EmailToken extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "email_token_id")
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private LocalDateTime expiredAt;

    @Column(nullable = false)
    private boolean verified;

    @Column(nullable = false)
    private boolean used;

    // 이메일 인증 완료 처리
    public void verify() {
        this.verified = true;
    }

    // 이메일 토큰 사용 처리
    public void use() {
        this.used = true;
    }

    // 이메일 토큰 만료 여부 확인
    public boolean isExpired() {
        return this.expiredAt.isBefore(LocalDateTime.now());
    }

    // 이메일 토큰이 사용 가능한 상태인지 확인
    public void validateUsable() {
        if (this.used) {
            throw new EmailTokenHandler(ErrorStatus.EMAIL_TOKEN_ALREADY_USED);
        }
        if (this.isExpired()) {
            throw new EmailTokenHandler(ErrorStatus.EMAIL_TOKEN_EXPIRED);
        }
    }
}
