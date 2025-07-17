package UMC_7th.Closit.domain.battle.dto.BattleDTO.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record ChallengeBattleRequest(
        @NotNull
        Long postId
) {
}
