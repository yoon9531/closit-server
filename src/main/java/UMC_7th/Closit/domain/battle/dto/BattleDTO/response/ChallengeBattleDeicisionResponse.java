package UMC_7th.Closit.domain.battle.dto.BattleDTO.response;

import UMC_7th.Closit.domain.battle.entity.Battle;
import UMC_7th.Closit.domain.battle.entity.enums.BattleStatus;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ChallengeBattleDeicisionResponse(
        BattleStatus battleStatus,
        LocalDateTime deadline,
        LocalDateTime updatedAt
) {
    public static ChallengeBattleDeicisionResponse from(Battle battle) {
        return ChallengeBattleDeicisionResponse.builder()
                .battleStatus(battle.getBattleStatus())
                .deadline(battle.getDeadline())
                .updatedAt(battle.getUpdatedAt())
                .build();
    }
}
