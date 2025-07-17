package UMC_7th.Closit.domain.battle.dto.BattleDTO.response;

import UMC_7th.Closit.domain.battle.entity.Battle;
import UMC_7th.Closit.domain.battle.entity.enums.BattleStatus;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record DecideChallengeResponse(
        BattleStatus battleStatus,
        LocalDateTime deadline,
        LocalDateTime updatedAt
) {
    public static DecideChallengeResponse from(Battle battle) {
        return DecideChallengeResponse.builder()
                .battleStatus(battle.getBattleStatus())
                .deadline(battle.getDeadline())
                .updatedAt(battle.getUpdatedAt())
                .build();
    }
}
