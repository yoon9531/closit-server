package UMC_7th.Closit.domain.battle.dto.BattleDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

public class BattleRequestDTO {

    @Getter
    public static class CreateBattleDTO { // 배틀 생성
        @NotNull
        private Long postId;
        @NotBlank
        private String title;
    }

    @Getter
    public static class ChallengeBattleDTO { // 배틀 신청
        @NotNull
        private Long postId;
    }

    @Getter
    public static class AcceptChallengeDTO { // 배틀 신청 수락
        @NotNull
        private Long challengeBattleId;
    }

    @Getter
    public static class VoteBattleDTO { // 배틀 투표
        @NotNull
        private Long postId;
    }
}
