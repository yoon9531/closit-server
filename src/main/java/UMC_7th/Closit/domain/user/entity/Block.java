package UMC_7th.Closit.domain.user.entity;

import UMC_7th.Closit.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(
        name = "user_block",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"blocker", "blocked"})
        },
        indexes = {
                @Index(name = "idx_blocker", columnList = "blocker"),
                @Index(name = "idx_blocked", columnList = "blocked")
        }
)
public class Block extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "blocker", nullable = false)
    private String blockerId;

    @Column(name = "blocked", nullable = false)
    private String blockedId;

}
