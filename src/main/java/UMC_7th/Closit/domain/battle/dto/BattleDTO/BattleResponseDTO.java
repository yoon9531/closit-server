package UMC_7th.Closit.domain.battle.dto.BattleDTO;

import UMC_7th.Closit.domain.battle.entity.enums.BattleStatus;
import UMC_7th.Closit.domain.battle.entity.enums.ChallengeStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class BattleResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BattlePreviewDTO { // 배틀 게시글 목록, 내가 투표한 게시글 목록 조회
        private Long battleId;
        private boolean isLiked;
        private int likeCount;
        private String title;
        private String firstClositId;
        private String firstProfileImage;
        private Long firstPostId;
        private String firstPostFrontImage;
        private String firstPostBackImage;
        private int firstVotingCnt;
        private String secondClositId;
        private String secondProfileImage;
        private Long secondPostId;
        private String secondPostFrontImage;
        private String secondPostBackImage;
        private int secondVotingCnt;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BattlePreviewListDTO {
        private List<BattlePreviewDTO> battlePreviewList;
        private Integer listSize;
        private boolean isFirst;
        private boolean isLast;
        private boolean hasNext;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChallengeBattlePreviewDTO { // 배틀 챌린지 게시글 목록 조회
        private Long battleId;
        private String firstClositId;
        private String firstProfileImage;
        private Long firstPostId;
        private String firstPostFrontImage;
        private String firstPostBackImage;
        private String title;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChallengeBattlePreviewListDTO {
        private List<ChallengeBattlePreviewDTO> challengeBattlePreviewList;
        private Integer listSize;
        private boolean isFirst;
        private boolean isLast;
        private boolean hasNext;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetChallengeBattleDTO {
        private Long battleId;
        private Long challengeBattleId;
        private String title;
        private String description;
        private String firstClositId;
        private String firstProfileImage;
        private Long firstPostId;
        private String firstPostFrontImage;
        private String firstPostBackImage;
        private String secondClositId;
        private String secondProfileImage;
        private Long secondPostId;
        private String secondPostFrontImage;
        private String secondPostBackImage;
        private ChallengeStatus challengeStatus;
    }
}