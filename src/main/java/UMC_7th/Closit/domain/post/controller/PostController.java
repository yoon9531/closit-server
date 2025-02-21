package UMC_7th.Closit.domain.post.controller;

import UMC_7th.Closit.domain.post.converter.PostConverter;
import UMC_7th.Closit.domain.post.dto.PostRequestDTO;
import UMC_7th.Closit.domain.post.dto.PostResponseDTO;
import UMC_7th.Closit.domain.post.entity.Post;
import UMC_7th.Closit.domain.post.service.PostCommandService;
import UMC_7th.Closit.domain.post.service.PostQueryService;
import UMC_7th.Closit.domain.user.entity.User;
import UMC_7th.Closit.global.apiPayload.ApiResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.print.attribute.standard.Media;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/posts")
public class PostController {

    private final PostQueryService postQueryService;
    private final PostCommandService postCommandService;

    @Operation(summary = "게시글 업로드")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE )
    public ApiResponse<PostResponseDTO.CreatePostResultDTO> createPost (
            @RequestPart @Valid
            PostRequestDTO.CreatePostDTO request, // 게시글 정보
            @RequestPart("frontImage")
            MultipartFile frontImage, // 앞면 이미지
            @RequestPart("backImage")
            MultipartFile backImage    // 뒷면 이미지
    ) {

        Post post = postCommandService.createPost(request, frontImage, backImage);

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

