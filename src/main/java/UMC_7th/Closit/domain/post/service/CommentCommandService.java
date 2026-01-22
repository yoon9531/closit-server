package UMC_7th.Closit.domain.post.service;

import UMC_7th.Closit.domain.post.dto.CommentRequestDTO;
import UMC_7th.Closit.domain.post.entity.Comment;

public interface CommentCommandService {
    Comment createComment(Long postId, CommentRequestDTO.CreateCommentRequestDTO request); // 댓글 생성
    void deleteComment(Long postId, Long commentId); // 댓글 삭제
}

