package UMC_7th.Closit.domain.battle.dto.BattleDTO.response;

import lombok.Builder;

@Builder
public record BattleListResponse(
        Long battleId,
        boolean isLiked,
        int likeCount,
        String title,
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
}
