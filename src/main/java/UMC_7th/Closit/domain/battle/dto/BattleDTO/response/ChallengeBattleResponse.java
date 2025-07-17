package UMC_7th.Closit.domain.battle.dto.BattleDTO.response;

import UMC_7th.Closit.domain.battle.entity.ChallengeBattle;
import UMC_7th.Closit.domain.battle.entity.enums.ChallengeStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ChallengeBattleResponse(
        Long challengeBattleId,
        String firstClositId,
        Long firstPostId,
        String firstPostFrontImage,
        String firstPostBackImage,
        String secondClositId,
        Long secondPostId,
        String secondPostFrontImage,
        String secondPostBackImage,
        ChallengeStatus challengeStatus,
        @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
        LocalDateTime createdAt
) {
    public static ChallengeBattleResponse from(ChallengeBattle challengeBattle) {
        return ChallengeBattleResponse.builder()
                .challengeBattleId(challengeBattle.getId())
                .firstClositId(challengeBattle.getBattle().getPost1().getUser().getClositId())
                .firstPostId(challengeBattle.getBattle().getPost1().getId())
                .firstPostFrontImage(challengeBattle.getBattle().getPost1().getFrontImage())
                .firstPostBackImage(challengeBattle.getBattle().getPost1().getBackImage())
                .secondClositId(challengeBattle.getPost().getUser().getClositId())
                .secondPostId(challengeBattle.getPost().getId())
                .secondPostFrontImage(challengeBattle.getPost().getFrontImage())
                .secondPostBackImage(challengeBattle.getPost().getBackImage())
                .challengeStatus(challengeBattle.getChallengeStatus())
                .createdAt(challengeBattle.getCreatedAt())
                .build();
    }
}
