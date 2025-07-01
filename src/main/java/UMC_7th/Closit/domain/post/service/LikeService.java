package UMC_7th.Closit.domain.post.service;

import UMC_7th.Closit.domain.post.dto.LikeResponseDTO;
import org.springframework.data.domain.Pageable;

public interface LikeService {
    LikeResponseDTO.LikeStatusDTO likePost(Long postId);
    LikeResponseDTO.LikeStatusDTO unlikePost(Long postId);
    LikeResponseDTO.LikedUserListDTO getLikedUsers(Long postId, Pageable pageable);
}
