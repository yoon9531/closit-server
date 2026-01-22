package UMC_7th.Closit.domain.post.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class CommentResponseDTO {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateCommentResultDTO { // 댓글 생성 응답
        private Long commentId;
        private String clositId;
        @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
        private LocalDateTime createdAt;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommentPreviewDTO { // 댓글 조회
        private String clositId;
        private Long commentId;
        private Long parentCommentId;
        private String content;
        private boolean isParent;
        private List<CommentPreviewDTO> replies;
        @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
        private LocalDateTime createdAt;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommentPreviewListDTO { // 댓글 리스트 조회
        private List<CommentPreviewDTO> commentPreviewList;
        private Integer listSize;
        private boolean isFirst;
        private boolean isLast;
        private boolean hasNext;
    }
}
