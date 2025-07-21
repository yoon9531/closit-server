package UMC_7th.Closit.domain.battle.dto.BattleDTO.response;

import UMC_7th.Closit.domain.battle.entity.Vote;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record BattleVoteResponse(
        Long battleId,
        String firstClositId,
        int firstVotingCnt,
        String secondClositId,
        int secondVotingCnt,
        LocalDateTime createdAt
) {
    public static BattleVoteResponse from(Vote vote) {
        return BattleVoteResponse.builder()
                .battleId(vote.getBattle().getId())
                .firstClositId(vote.getBattle().getPost1().getUser().getClositId())
                .firstVotingCnt(vote.getBattle().getFirstVotingCnt())
                .secondClositId(vote.getBattle().getPost2().getUser().getClositId())
                .secondVotingCnt(vote.getBattle().getSecondVotingCnt())
                .createdAt(vote.getBattle().getCreatedAt())
                .build();
    }
}
