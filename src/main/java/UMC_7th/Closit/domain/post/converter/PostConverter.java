package UMC_7th.Closit.domain.post.converter;

import UMC_7th.Closit.domain.post.dto.PostRequestDTO;
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

    public static PostRequestDTO.CreatePostDTO toPost(Post post) {
        return PostRequestDTO.CreatePostDTO.builder()
                .frontImage(post.getFrontImage())
                .backImage(post.getBackImage())
                .pointColor(post.getPointColor())             // 포인트 컬러
                .visibility(post.getVisibility())             // 공개 여부
                .isMission(post.isMission())
                .hashtags(post.getPostHashtagList().stream()  // 해시태그
                        .map(postHashtag -> postHashtag.getHashtag().getContent())
                        .collect(Collectors.toList()))
                .frontItemtags(post.getItemTagList().stream() // Front ItemTags
                        .filter(itemTag -> "FRONT".equals(itemTag.getTagType()))
                        .map(itemTag -> PostResponseDTO.ItemTagDTO.builder()
                                .x(itemTag.getItemTagX())
                                .y(itemTag.getItemTagY())
                                .content(itemTag.getItemTagContent())
                                .build())
                        .collect(Collectors.toList()))
                .backItemtags(post.getItemTagList().stream()  // Back ItemTags
                        .filter(itemTag -> "BACK".equals(itemTag.getTagType()))
                        .map(itemTag -> PostResponseDTO.ItemTagDTO.builder()
                                .x(itemTag.getItemTagX())
                                .y(itemTag.getItemTagY())
                                .content(itemTag.getItemTagContent())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    public static PostResponseDTO.createPresignedUrlDTO getPresignedUrlDTO(String frontImageUrl, String backImageUrl) {
        return PostResponseDTO.createPresignedUrlDTO.builder()
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

