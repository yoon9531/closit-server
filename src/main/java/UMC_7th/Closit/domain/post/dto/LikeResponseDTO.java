package UMC_7th.Closit.domain.post.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class LikeResponseDTO {
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LikeStatusDTO{
        private Boolean isLiked;
        private Long postId;
        private String clositId;
    }
}
