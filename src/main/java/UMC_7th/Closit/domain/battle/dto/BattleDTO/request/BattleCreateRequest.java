package UMC_7th.Closit.domain.battle.dto.BattleDTO.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record BattleCreateRequest(
        @NotNull
        Long postId,

        @NotBlank
        String title,

        String description
) {
}
