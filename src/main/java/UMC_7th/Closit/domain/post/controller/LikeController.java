package UMC_7th.Closit.domain.post.controller;

import UMC_7th.Closit.domain.post.dto.LikeResponseDTO;
import UMC_7th.Closit.domain.post.service.LikeService;
import UMC_7th.Closit.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts/{post_id}/likes")
public class LikeController {
    private final LikeService likeService;

    @Operation(summary = "게시글 좋아요 추가")
    @PostMapping
    public ApiResponse<LikeResponseDTO.LikeStatusDTO> likePost(@PathVariable("post_id") Long postId) {
        return ApiResponse.onSuccess(likeService.likePost(postId));
    }

    @Operation(summary = "게시글 좋아요 삭제")
    @DeleteMapping
    public ApiResponse<LikeResponseDTO.LikeStatusDTO> unlikePost(@PathVariable("post_id") Long postId){
        return ApiResponse.onSuccess(likeService.unlikePost(postId));
    }
}
