package UMC_7th.Closit.domain.post.converter;

import UMC_7th.Closit.domain.post.dto.LikeResponseDTO;
import UMC_7th.Closit.domain.post.entity.Likes;
import UMC_7th.Closit.domain.post.entity.Post;
import UMC_7th.Closit.domain.user.entity.User;
import org.springframework.data.domain.Slice;

import java.util.List;

public class LikeConverter {
    public static LikeResponseDTO.LikeStatusDTO toLikeStatusDTO(Post post, User user, boolean isLiked) {
        return LikeResponseDTO.LikeStatusDTO.builder()
                .postId(post.getId())
                .clositId(user.getClositId())
                .isLiked(isLiked)
                .build();
    }

    public static LikeResponseDTO.LikedUserDTO toLikedUserDTO(User user) {
        return LikeResponseDTO.LikedUserDTO.builder()
                .clositId(user.getClositId())
                .name(user.getName())
                .profileImage(user.getProfileImage())
                .build();
    }

    public static LikeResponseDTO.LikedUserListDTO toLikedUserListDTO(Slice<Likes> slice) {
        List<LikeResponseDTO.LikedUserDTO> data = slice.stream()
                .map(like -> toLikedUserDTO(like.getUser()))
                .toList();

        return LikeResponseDTO.LikedUserListDTO.builder()
                .data(data)
                .hasNext(slice.hasNext())
                .build();
    }
}
