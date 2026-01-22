package UMC_7th.Closit.domain.post.dto;

import UMC_7th.Closit.domain.post.entity.Visibility;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class PostResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetPresignedUrlDTO { // 게시글 presigned Url 발급
        private String frontImageUrl;
        private String backImageUrl;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreatePostResultDTO { // 게시글 업로드 응답 DTO
        private String clositId;
        private Long postId;
        private String frontImage;
        private String backImage;
        private Visibility visibility;
        @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
        private LocalDateTime createdAt;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostPreviewDTO { // 게시글 목록 조회
        private Long postId;
        private String clositId;
        private String userName;
        private String profileImage;
        private String frontImage;
        private String backImage;
        private Boolean isLiked;
        private Boolean isSaved;
        private Boolean isHighlighted;
        private List<HashtagDTO> hashtags;
        private List<ItemTagDTO> frontItemtags;
        private List<ItemTagDTO> backItemtags;
        private String pointColor;
        private Visibility visibility;
        private boolean isMission;
        @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
        private LocalDateTime createdAt;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostPreviewListDTO {
        private List<PostPreviewDTO> postPreviewList;
        private Integer listSize;
        private boolean isFirst;
        private boolean isLast;
        private boolean hasNext;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdatePostResultDTO { // 게시글 업로드 응답 DTO
        private String clositId;
        private Long postId;
        private Visibility visibility;
        @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
        private LocalDateTime createdAt;
    }

}
