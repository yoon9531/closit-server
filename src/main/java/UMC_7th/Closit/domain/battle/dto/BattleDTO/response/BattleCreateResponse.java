package UMC_7th.Closit.domain.battle.dto.BattleDTO.response;

import UMC_7th.Closit.domain.battle.entity.Battle;
import UMC_7th.Closit.domain.battle.entity.enums.BattleStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record BattleCreateResponse(
        Long battleId,
        String thumbnail,
        BattleStatus battleStatus,
        @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
        LocalDateTime createdAt
) {
    public static BattleCreateResponse from(Battle battle) {
        return BattleCreateResponse.builder()
                .battleId(battle.getId())
                .thumbnail(battle.getPost1().getFrontImage())
                .battleStatus(battle.getBattleStatus())
                .createdAt(battle.getCreatedAt())
                .build();
    }
}
