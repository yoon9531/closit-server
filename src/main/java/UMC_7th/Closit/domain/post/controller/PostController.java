package UMC_7th.Closit.domain.post.controller;

import UMC_7th.Closit.domain.post.converter.PostConverter;
import UMC_7th.Closit.domain.post.dto.PostRequestDTO;
import UMC_7th.Closit.domain.post.dto.PostResponseDTO;
import UMC_7th.Closit.domain.post.entity.Post;
import UMC_7th.Closit.domain.post.entity.PostSorting;
import UMC_7th.Closit.domain.post.service.PostCommandService;
import UMC_7th.Closit.domain.post.service.PostQueryService;
import UMC_7th.Closit.domain.user.entity.User;
import UMC_7th.Closit.global.apiPayload.ApiResponse;
import UMC_7th.Closit.global.s3.S3Service;
import UMC_7th.Closit.security.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostController {

    private final SecurityUtil securityUtil;
    private final S3Service s3Service;
    private final PostQueryService postQueryService;
    private final PostCommandService postCommandService;

    @Value("${cloud.aws.s3.path.post-front}")
    private String postFrontPath;

    @Value("${cloud.aws.s3.path.post-back}")
    private String postBackPath;

    @PostMapping("/presigned-url")
    @Operation(summary = "게시글 Presigned Url 발급",
            description = """
                    ## 게시글 등록을 위한 Presigned Url 발급
                    ### RequestBody
                    frontImageUrl [전면 이미지 파일명] \n
                    backImageUrl [후면 이미지 파일명]
                    ### 등록 과정
                    1. 등록할 게시글 이미지 파일명 작성
                    2. 발급 받은 Presigned Url + 실제 파일 (Body -> binary)과 함께 PUT 요청 (Postman에서 진행)
                    3. 200 OK -> S3 업로드 성공
                    4. 성공 후, 게시글 업로드 API에 Presigned Url의 쿼리 파라미터를 제외하고 업로드 요청
                    """)
    public ApiResponse<PostResponseDTO.GetPresignedUrlDTO> getPresignedUrl(@RequestBody @Valid PostRequestDTO.GetPresignedUrlDTO request) {
        User user = securityUtil.getCurrentUser();

        String frontImageUrl = s3Service.getPresignedUrl(postFrontPath, request.getFrontImageUrl());
        String backImageUrl = s3Service.getPresignedUrl(postBackPath, request.getBackImageUrl());

        return ApiResponse.onSuccess(PostConverter.getPresignedUrlDTO(frontImageUrl, backImageUrl));
    }

    @Operation(summary = "게시글 업로드")
    @PostMapping()
    public ApiResponse<PostResponseDTO.CreatePostResultDTO> createPost(@RequestBody @Valid PostRequestDTO.CreatePostDTO request) {
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
            @RequestParam(defaultValue = "10") int size,
            @Parameter(
                    name = "sort",
                    description = "정렬 기준: LATEST(최신순), VIEW(조회순)",
                    example = "LATEST",
                    schema = @Schema(allowableValues = {"LATEST", "VIEW"})
            )
            @RequestParam(defaultValue = "LATEST") String sort
    ) {
        PostSorting sortType = PostSorting.valueOf(sort);
        Pageable pageable = PageRequest.of(page, size, sortType.getSort());
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

