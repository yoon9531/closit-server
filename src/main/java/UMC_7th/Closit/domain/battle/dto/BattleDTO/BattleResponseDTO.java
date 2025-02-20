package UMC_7th.Closit.domain.battle.dto.BattleDTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class BattleResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateBattleResultDTO { // 배틀 생성
        private Long battleId;
        private String thumbnail;
        @JsonFormat(pattern = "yyyy/MM/dd")
        private LocalDate deadline;
        @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
        private LocalDateTime createdAt;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChallengeBattleResultDTO { // 배틀 신청
        private String firstClositId;
        private Long firstPostId;
        private String firstPostFrontImage;
        private String firstPostBackImage;
        private String secondClositId;
        private Long secondPostId;
        private String secondPostFrontImage;
        private String secondPostBackImage;
        @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
        private LocalDateTime createdAt;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VoteBattleResultDTO { // 배틀 투표
        private Long battleId;
        private String firstClositId;
        private double firstVotingRate;
        private String secondClositId;
        private double secondVotingRate;
        @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
        private LocalDateTime createdAt;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BattlePreviewDTO { // 배틀 게시글 목록 조회
        private Long battleId;
        private boolean isLiked;
        private String title;
        private String firstClositId;
        private String firstProfileImage;
        private Long firstPostId;
        private String firstPostFrontImage;
        private String firstPostBackImage;
        private double firstVotingRate;
        private String secondClositId;
        private String secondProfileImage;
        private Long secondPostId;
        private String secondPostFrontImage;
        private String secondPostBackImage;
        private double secondVotingRate;
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
}