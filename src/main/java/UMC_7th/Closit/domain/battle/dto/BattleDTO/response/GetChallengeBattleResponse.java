package UMC_7th.Closit.domain.battle.dto.BattleDTO.response;

import UMC_7th.Closit.domain.battle.entity.ChallengeBattle;
import UMC_7th.Closit.domain.battle.entity.enums.ChallengeStatus;
import lombok.Builder;

@Builder
public record GetChallengeBattleResponse(
        Long battleId,
        Long challengeBattleId,
        String title,
        String description,
        String firstClositId,
        String firstProfileImage,
        Long firstPostId,
        String firstPostFrontImage,
        String firstPostBackImage,
        String secondClositId,
        String secondProfileImage,
        Long secondPostId,
        String secondPostFrontImage,
        String secondPostBackImage,
        ChallengeStatus challengeStatus
) {
    public static GetChallengeBattleResponse from(ChallengeBattle challengeBattle) {
        return GetChallengeBattleResponse.builder()
                .battleId(challengeBattle.getBattle().getId())
                .challengeBattleId(challengeBattle.getId())
                .title(challengeBattle.getBattle().getTitle())
                .description(challengeBattle.getBattle().getDescription())
                .firstClositId(challengeBattle.getBattle().getPost1().getUser().getClositId())
                .firstProfileImage(challengeBattle.getBattle().getPost1().getUser().getProfileImage())
                .firstPostId(challengeBattle.getBattle().getPost1().getId())
                .firstPostFrontImage(challengeBattle.getBattle().getPost1().getFrontImage())
                .firstPostBackImage(challengeBattle.getBattle().getPost1().getBackImage())
                .secondClositId(challengeBattle.getPost().getUser().getClositId())
                .secondProfileImage(challengeBattle.getPost().getUser().getProfileImage())
                .secondPostId(challengeBattle.getPost().getId())
                .secondPostFrontImage(challengeBattle.getPost().getFrontImage())
                .secondPostBackImage(challengeBattle.getPost().getBackImage())
                .challengeStatus(challengeBattle.getChallengeStatus())
                .build();
    }
}
