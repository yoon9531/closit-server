package UMC_7th.Closit.domain.post.controller;

import UMC_7th.Closit.domain.post.dto.LikeResponseDTO;
import UMC_7th.Closit.domain.post.dto.PostResponseDTO;
import UMC_7th.Closit.domain.post.service.LikeService;
import UMC_7th.Closit.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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

    @Operation(summary = "게시글 좋아요한 유저 목록 조회")
    @GetMapping("/list")
    public ApiResponse<LikeResponseDTO.LikedUserListDTO> getLikedUsers(
            @PathVariable("post_id") Long postId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ApiResponse.onSuccess(likeService.getLikedUsers(postId, pageable));
    }
}
