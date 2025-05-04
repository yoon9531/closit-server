package UMC_7th.Closit.domain.battle.entity;

import UMC_7th.Closit.domain.post.entity.Post;
import UMC_7th.Closit.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(
        name = "challenge_battle",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_battle_post", columnNames = {"battle_id", "post_id"})
        }
)
public class ChallengeBattle extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "challenge_battle_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChallengeStatus challengeStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "battle_id", nullable = false)
    private Battle battle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    public void rejectBattle() { // 배틀 거절
        this.challengeStatus = ChallengeStatus.REJECTED;
    }
}
