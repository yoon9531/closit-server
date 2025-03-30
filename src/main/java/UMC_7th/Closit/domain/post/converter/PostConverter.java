package UMC_7th.Closit.domain.post.converter;

import UMC_7th.Closit.domain.post.dto.PostResponseDTO;
import UMC_7th.Closit.domain.post.entity.ItemTag;
import UMC_7th.Closit.domain.post.entity.Post;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.stream.Collectors;

public class PostConverter {

    public static PostResponseDTO.PostPreviewDTO toPostPreviewDTO (Post post, Boolean isLiked, Boolean isSaved, Boolean isHighlighted,
                                                                   List<String> hashtags, List<ItemTag> frontTags, List<ItemTag> backTags) {

        List<PostResponseDTO.ItemTagDTO> frontItemtags = frontTags.stream()
                .map(tag -> PostResponseDTO.ItemTagDTO.builder()
                        .x(tag.getItemTagX())
                        .y(tag.getItemTagY())
                        .content(tag.getItemTagContent())
                        .build())
                .collect(Collectors.toList());

        List<PostResponseDTO.ItemTagDTO> backItemtags = backTags.stream()
                .map(tag -> PostResponseDTO.ItemTagDTO.builder()
                        .x(tag.getItemTagX())
                        .y(tag.getItemTagY())
                        .content(tag.getItemTagContent())
                        .build())
                .collect(Collectors.toList());

        return PostResponseDTO.PostPreviewDTO.builder()
                .postId(post.getId())
                .clositId(post.getUser().getClositId())
                .userName(post.getUser().getName())
                .profileImage(post.getUser().getProfileImage())
                .frontImage(post.getFrontImage())
                .backImage(post.getBackImage())
                .isLiked(isLiked)
                .isSaved(isSaved)
                .isHighlighted(isHighlighted)
                .hashtags(hashtags)
                .frontItemtags(frontItemtags)
                .backItemtags(backItemtags)
                .pointColor(post.getPointColor())
                .visibility(post.getVisibility())
                .isMission(post.isMission())
                .build();
    }

    public static PostResponseDTO.PostPreviewListDTO toPostPreviewListDTO(Slice<PostResponseDTO.PostPreviewDTO> posts) {
        return PostResponseDTO.PostPreviewListDTO.builder()
                .postPreviewList(posts.getContent())
                .listSize(posts.getNumberOfElements())
                .isFirst(posts.isFirst())
                .isLast(posts.isLast())
                .hasNext(posts.hasNext())
                .build();
    }

    public static PostResponseDTO.GetPresignedUrlDTO getPresignedUrlDTO(String frontImageUrl, String backImageUrl) {
        return PostResponseDTO.GetPresignedUrlDTO.builder()
                .frontImageUrl(frontImageUrl)
                .backImageUrl(backImageUrl)
                .build();
    }

    public static PostResponseDTO.CreatePostResultDTO toCreatePostResultDTO(Post post) {
        return PostResponseDTO.CreatePostResultDTO.builder()
                .clositId(post.getUser().getClositId())
                .postId(post.getId())
                .frontImage(post.getFrontImage())
                .backImage(post.getBackImage())
                .createdAt(post.getCreatedAt())
                .visibility(post.getVisibility())
                .build();
    }

    public static PostResponseDTO.UpdatePostResultDTO toUpdatePostResultDTO(Post post) {
        return PostResponseDTO.UpdatePostResultDTO.builder()
                .clositId(post.getUser().getClositId())
                .postId(post.getId())
                .createdAt(post.getCreatedAt())
                .visibility(post.getVisibility())
                .build();
    }
}

