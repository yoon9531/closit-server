package UMC_7th.Closit.domain.post.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class HistoryResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DateHistoryThumbnailDTO { // 히스토리 썸네일 조회
        private Long postId;
        private String thumbnail;
        @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
        private LocalDateTime createdAt;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DataHistoryThumbnailListDTO {
        private List<DateHistoryThumbnailDTO> dateHistoryThumbnailDTOList;
        private Integer listSize;
        private boolean isFirst;
        private boolean isLast;
        private boolean hasNext;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DateHistoryPreviewDTO { // 히스토리 게시글 상세 조회
        private Long postId;
        @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
        private LocalDateTime createdAt;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DateHistoryPreviewListDTO {
        private List<DateHistoryPreviewDTO> postList;
        @JsonFormat(pattern = "yyyy/MM/dd")
        private LocalDate date;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ColorHistoryThumbnailDTO { // 히스토리 포인트 색상 썸네일 조회
        private Long postId;
        private String thumbnail;
        @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
        private LocalDateTime createdAt;
    }
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ColorHistoryThumbnailListDTO {
        private List<ColorHistoryThumbnailDTO> colorHistoryThumbnailDTOList;
        private Integer listSize;
        private boolean isFirst;
        private boolean isLast;
        private boolean hasNext;
    }
}
