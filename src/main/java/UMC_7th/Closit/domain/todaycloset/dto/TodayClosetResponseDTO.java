package UMC_7th.Closit.domain.todaycloset.dto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

public class TodayClosetResponseDTO {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class CreateResponseDTO {
        private Long todayClosetId;
        @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
        private LocalDateTime createdAt;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class TodayClosetPreviewDTO {
        private Long todayClosetId;
        private Long postId;
        private String frontImage;
        private String backImage;
        private Integer viewCount;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class TodayClosetListDTO {
        private List<TodayClosetPreviewDTO> todayClosets;
        private Integer listSize;
        private boolean isFirst;
        private boolean isLast;
        private boolean hasNext;
    }
}