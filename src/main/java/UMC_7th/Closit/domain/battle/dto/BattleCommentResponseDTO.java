package UMC_7th.Closit.domain.battle.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class BattleCommentResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateBattleCommentDTO { // 배틀 댓글 생성
        private Long battleCommentId;
        private Long parentBattleCommentId;
        private String clositId;
        private String thumbnail;
        @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
        private LocalDateTime createdAt;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BattleCommentPreviewDTO { // 배틀 댓글 조회
        private Long battleCommentId;
        private Long parentBattleCommentId;
        private String clositId;
        private String thumbnail;
        private String content;
        private List<BattleCommentPreviewDTO> childrenBattleComments;
        @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
        private LocalDateTime createdAt;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BattleCommentPreviewListDTO {
        private List<BattleCommentPreviewDTO> battleCommentPreviewList;
        private Integer listSize;
        private boolean isFirst;
        private boolean isLast;
        private boolean hasNext;
    }
}
