package UMC_7th.Closit.domain.post.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

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

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LikedUserDTO {
        private String clositId;
        private String name;
        private String profileImage;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LikedUserListDTO {
        private List<LikedUserDTO> data;
        private boolean hasNext;
    }
}
