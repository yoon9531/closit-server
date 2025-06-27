package UMC_7th.Closit.domain.post.controller;

import UMC_7th.Closit.domain.post.converter.CommentConverter;
import UMC_7th.Closit.domain.post.dto.CommentRequestDTO;
import UMC_7th.Closit.domain.post.dto.CommentResponseDTO;
import UMC_7th.Closit.domain.post.entity.Comment;
import UMC_7th.Closit.domain.post.service.CommentCommandService;
import UMC_7th.Closit.domain.post.service.CommentQueryService;
import UMC_7th.Closit.domain.user.entity.User;
import UMC_7th.Closit.global.apiPayload.ApiResponse;
import UMC_7th.Closit.security.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts/{post_id}/comments")
public class CommentController {

    private final CommentCommandService commentCommandService;
    private final CommentQueryService commentQueryService;

    @PostMapping
    @Operation(summary = "새로운 댓글 생성",
            description = """
            ## 게시글 내 새로운 댓글 작성
            ### PathVariable
            post_id [게시글 ID] \n
            ### RequestBody
            - `content` (String): 댓글 내용 (필수)
            - `parentId` (Long, optional): 대댓글의 경우 부모 댓글 ID를 입력하세요.
            - `parentId`가 `null`일시 → 일반 댓글(부모 댓글)로 처리됩니다.
            - `parentId`가 존재할 경우:
            - 해당 ID는 반드시 같은 게시글의 댓글이어야 합니다.
            - 부모 댓글의 대댓글까지만 허용되며, 대댓글의 대댓글은 작성할 수 없습니다.
            """)
    public ApiResponse<CommentResponseDTO.CreateCommentResultDTO> createComment(
            @RequestBody @Valid CommentRequestDTO.CreateCommentRequestDTO request,
            @PathVariable("post_id") Long postId) {

        Comment comment = commentCommandService.createComment(postId, request);

        return ApiResponse.onSuccess(CommentConverter.createCommentResponseDTO(comment));
    }

    @GetMapping
    @Operation(summary = "댓글 조회",
            description = """
            ## 게시글 내 댓글 목록 조회
            ### PathVariable
            post_id [게시글 ID] \n
            ### RequestParam
            page [조회할 페이지 번호] - 0부터 시작, 10개씩 보여줌
            """)
    public ApiResponse<CommentResponseDTO.CommentPreviewListDTO> getComments(
            @PathVariable("post_id") Long postId,
            @RequestParam(name = "page") Integer page) {

        Slice<Comment> commentList = commentQueryService.getCommentList(postId, page);

        return ApiResponse.onSuccess(CommentConverter.commentPreviewListDTO(commentList));
    }

    @DeleteMapping("/{comment_id}")
    @Operation(summary = "댓글 삭제",
            description = """
            ## 게시글 내 특정 댓글 삭제
            ### PathVariable
            post_id [게시글 ID] \n
            comment_id [댓글 ID]
            """)
    public ApiResponse<String> deleteComment(
            @PathVariable("post_id") Long postId,
            @PathVariable("comment_id") Long commentId) {

        commentCommandService.deleteComment(postId, commentId);

        return ApiResponse.onSuccess("Deletion successful");
    }
}
