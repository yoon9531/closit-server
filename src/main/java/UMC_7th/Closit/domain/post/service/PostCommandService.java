package UMC_7th.Closit.domain.post.service;

import UMC_7th.Closit.domain.post.dto.PostRequestDTO;
import UMC_7th.Closit.domain.post.entity.Post;

public interface PostCommandService {

    Post createPost(PostRequestDTO.CreatePostDTO request);

    Post updatePost(Long postId, PostRequestDTO.UpdatePostDTO request);

    void deletePost(Long postId);
}

