package UMC_7th.Closit.domain.battle.dto.BattleDTO.response;

import UMC_7th.Closit.domain.battle.entity.Battle;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record BattleDetailResponse(
        Long battleId,
        String title,
        String description,
        @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
        LocalDateTime deadline,
        String firstClositId,
        String firstProfileImage,
        Long firstPostId,
        String firstPostFrontImage,
        String firstPostBackImage,
        int firstVotingCnt,
        String secondClositId,
        String secondProfileImage,
        Long secondPostId,
        String secondPostFrontImage,
        String secondPostBackImage,
        int secondVotingCnt
) {
    public static BattleDetailResponse from(Battle battle) {
        return BattleDetailResponse.builder()
                .battleId(battle.getId())
                .title(battle.getTitle())
                .description(battle.getDescription())
                .deadline(battle.getDeadline())
                .firstClositId(battle.getPost1().getUser().getClositId())
                .firstProfileImage(battle.getPost1().getUser().getProfileImage())
                .firstPostId(battle.getPost1().getId())
                .firstPostFrontImage(battle.getPost1().getFrontImage())
                .firstPostBackImage(battle.getPost1().getBackImage())
                .firstVotingCnt(battle.getFirstVotingCnt())
                .secondClositId(battle.getPost2().getUser().getClositId())
                .secondProfileImage(battle.getPost2().getUser().getProfileImage())
                .secondPostId(battle.getPost2().getId())
                .secondPostFrontImage(battle.getPost2().getFrontImage())
                .secondPostBackImage(battle.getPost2().getBackImage())
                .secondVotingCnt(battle.getSecondVotingCnt())
                .build();
    }
}
