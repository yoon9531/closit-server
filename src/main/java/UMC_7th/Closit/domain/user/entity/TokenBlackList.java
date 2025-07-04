package UMC_7th.Closit.domain.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Builder
@Table(
        name = "token_black_list",
        indexes = {
                @Index(name = "idx_access_token", columnList = "accessToken")
        }
)
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TokenBlackList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500)
    private String accessToken;

    @Column(nullable = false)
    private String clositId;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime expiredAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.expiredAt = LocalDateTime.now().plusHours(7);
    }
}
