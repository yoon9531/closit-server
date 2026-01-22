package UMC_7th.Closit.domain.post.service;

import UMC_7th.Closit.domain.post.dto.LikeResponseDTO;

public interface LikeService {
    LikeResponseDTO.LikeStatusDTO likePost(Long postId);
    LikeResponseDTO.LikeStatusDTO unlikePost(Long postId);
}
