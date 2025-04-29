package UMC_7th.Closit.domain.post.dto;

import UMC_7th.Closit.domain.post.entity.Visibility;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class PostRequestDTO {

    @Getter
    public static class GetPresignedUrlDTO { // 게시글 presigned Url 발급
        private String frontImageUrl;
        private String backImageUrl;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HashtagDTO {
        @Size(max = 20, message = "해시태그는 20자 이내여야 합니다.")
        private String content;
    }

    @Getter
    @Builder
    public static class CreatePostDTO{
        private String frontImage;
        private String backImage;
        private List<HashtagDTO> hashtags;
        private List<PostResponseDTO.ItemTagDTO> frontItemtags;
        private List<PostResponseDTO.ItemTagDTO> backItemtags;
        private String pointColor;
        private Visibility visibility;
        private boolean isMission;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UpdatePostDTO {
        private String frontImage;
        private String backImage;
        private List<HashtagDTO> hashtags;
        private List<PostResponseDTO.ItemTagDTO> frontItemtags;
        private List<PostResponseDTO.ItemTagDTO> backItemtags;
        private String pointColor;
        private Visibility visibility;
    }
}
