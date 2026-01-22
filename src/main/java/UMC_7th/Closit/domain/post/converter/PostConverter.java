package UMC_7th.Closit.domain.post.converter;

import UMC_7th.Closit.domain.post.dto.HashtagDTO;
import UMC_7th.Closit.domain.post.dto.ItemTagDTO;
import UMC_7th.Closit.domain.post.dto.PostResponseDTO;
import UMC_7th.Closit.domain.post.entity.Post;
import UMC_7th.Closit.domain.post.entity.PostHashtag;
import UMC_7th.Closit.domain.post.entity.PostItemTag;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.stream.Collectors;

public class PostConverter {

    public static PostResponseDTO.PostPreviewDTO toPostPreviewDTO (Post post, Boolean isLiked, Boolean isSaved, Boolean isHighlighted,
                                                                   List<PostHashtag> hashtags, List<PostItemTag> frontTags, List<PostItemTag> backTags) {

        List<HashtagDTO> hashtagDTOs = hashtags.stream()
                .map(tag -> HashtagDTO.builder()
                        .content(tag.getHashtag().getContent())
                        .build())
                .collect(Collectors.toList());

        List<ItemTagDTO> frontItemtags = frontTags.stream()
                .map(tag -> ItemTagDTO.builder()
                        .x(tag.getItemTagX())
                        .y(tag.getItemTagY())
                        .content(tag.getItemTag().getContent())
                        .build())
                .collect(Collectors.toList());

        List<ItemTagDTO> backItemtags = backTags.stream()
                .map(tag -> ItemTagDTO.builder()
                        .x(tag.getItemTagX())
                        .y(tag.getItemTagY())
                        .content(tag.getItemTag().getContent())
                        .build())
                .collect(Collectors.toList());

        return PostResponseDTO.PostPreviewDTO.builder()
                .postId(post.getId())
                .clositId(post.getUser().getClositId())
                .likeCount(post.getLikes())
                .profileImage(post.getUser().getProfileImage())
                .frontImage(post.getFrontImage())
                .backImage(post.getBackImage())
                .isLiked(isLiked)
                .isSaved(isSaved)
                .isHighlighted(isHighlighted)
                .hashtags(hashtagDTOs)
                .frontItemtags(frontItemtags)
                .backItemtags(backItemtags)
                .pointColor(post.getPointColor())
                .visibility(post.getVisibility())
                .isMission(post.isMission())
                .createdAt(post.getCreatedAt())
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

