package UMC_7th.Closit.domain.post.controller;

import UMC_7th.Closit.domain.post.converter.PostConverter;
import UMC_7th.Closit.domain.post.dto.PostRequestDTO;
import UMC_7th.Closit.domain.post.dto.PostResponseDTO;
import UMC_7th.Closit.domain.post.entity.Post;
import UMC_7th.Closit.domain.post.service.PostCommandService;
import UMC_7th.Closit.domain.post.service.PostQueryService;
import UMC_7th.Closit.domain.user.entity.User;
import UMC_7th.Closit.global.apiPayload.ApiResponse;
import UMC_7th.Closit.global.s3.S3Service;
import UMC_7th.Closit.security.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/posts")
public class PostController {

    private final SecurityUtil securityUtil;
    private final S3Service s3Service;
    private final PostQueryService postQueryService;
    private final PostCommandService postCommandService;

    @Value("${cloud.aws.s3.path.post-front}")
    private String postFrontPath;

    @Value("${cloud.aws.s3.path.post-back}")
    private String postBackPath;

    @Operation(summary = "게시글 Presigned URL 생성")
    @PostMapping("/presigned-url")
    public ApiResponse<PostResponseDTO.createPresignedUrlDTO> getPresignedUrl(@RequestBody @Valid PostRequestDTO.createPresignedUrlDTO request) {
        User user = securityUtil.getCurrentUser();

        String frontImageUrl = s3Service.getPresignedUrl(postFrontPath, request.getFrontImageUrl());
        String backImageUrl = s3Service.getPresignedUrl(postBackPath, request.getBackImageUrl());

        return ApiResponse.onSuccess(PostConverter.getPresignedUrlDTO(frontImageUrl, backImageUrl));
    }

    @Operation(summary = "게시글 업로드")
    @PostMapping()
    public ApiResponse<PostResponseDTO.CreatePostResultDTO> createPost(@RequestPart @Valid PostRequestDTO.CreatePostDTO request) {
        Post post = postCommandService.createPost(request);

        return ApiResponse.onSuccess(PostConverter.toCreatePostResultDTO(post));
    }

    @Operation(summary = "특정 게시글 조회")
    @GetMapping("/{post_id}")
    public ApiResponse<PostResponseDTO.PostPreviewDTO> getPostById(@PathVariable("post_id") Long postId) {

        PostResponseDTO.PostPreviewDTO postPreviewDTO = postQueryService.getPostById(postId);

        return ApiResponse.onSuccess(postPreviewDTO);
    }

    @Operation(summary = "팔로우 기반 게시글 조회")
    @GetMapping
    public ApiResponse<PostResponseDTO.PostPreviewListDTO> getPostListByFollowing(
            @RequestParam(name = "follower", defaultValue = "false") boolean follower,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Slice<PostResponseDTO.PostPreviewDTO> posts = postQueryService.getPostListByFollowing(follower, pageable);

        return ApiResponse.onSuccess(PostConverter.toPostPreviewListDTO(posts));
    }

    @Operation(summary = "해시태그 기반 게시글 검색")
    @GetMapping("/hashtag")
    public ApiResponse<PostResponseDTO.PostPreviewListDTO> getPostListByHashtag(
            @RequestParam(name = "hashtag", required = false) String hashtag,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Slice<PostResponseDTO.PostPreviewDTO> posts = postQueryService.getPostListByHashtag(hashtag, pageable);

        return ApiResponse.onSuccess(PostConverter.toPostPreviewListDTO(posts));
    }

    @Operation(summary = "게시글 수정")
    @PutMapping("/{post_id}")
    public ApiResponse<PostResponseDTO.UpdatePostResultDTO> updatePost(@PathVariable("post_id") Long postId,
                                        @RequestBody @Valid PostRequestDTO.UpdatePostDTO request) {
        Post post = postCommandService.updatePost(postId, request);
        return ApiResponse.onSuccess(PostConverter.toUpdatePostResultDTO(post));
    }

    @Operation(summary = "게시글 삭제")
    @DeleteMapping("/{post_id}")
    public ApiResponse<Void> deletePost(@PathVariable("post_id") Long postId) {
        postCommandService.deletePost(postId);
        return ApiResponse.onSuccess(null);
    }
}

