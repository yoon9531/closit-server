package UMC_7th.Closit.domain.post.service;

import UMC_7th.Closit.domain.notification.service.NotiCommandService;
import UMC_7th.Closit.domain.post.converter.CommentConverter;
import UMC_7th.Closit.domain.post.dto.CommentRequestDTO;
import UMC_7th.Closit.domain.post.entity.Comment;
import UMC_7th.Closit.domain.post.entity.Post;
import UMC_7th.Closit.domain.post.repository.CommentRepository;
import UMC_7th.Closit.domain.post.repository.PostRepository;
import UMC_7th.Closit.domain.user.entity.User;
import UMC_7th.Closit.domain.user.repository.UserRepository;
import UMC_7th.Closit.global.apiPayload.code.status.ErrorStatus;
import UMC_7th.Closit.global.apiPayload.exception.GeneralException;
import UMC_7th.Closit.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentCommandServiceImpl implements CommentCommandService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final NotiCommandService notiCommandService;
    private final SecurityUtil securityUtil;

    @Override
    @Transactional
    public Comment createComment(Long postId, CommentRequestDTO.CreateCommentRequestDTO request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.POST_NOT_FOUND));

        User user = securityUtil.getCurrentUser();

        Comment parent = null;

        if (request.getParentId() != null) {
            parent = commentRepository.findById(request.getParentId())
                    .orElseThrow(() -> new GeneralException(ErrorStatus.COMMENT_NOT_FOUND));
            if (parent.getParent() != null) {
                throw new GeneralException(ErrorStatus.REPLY_DEPTH_EXCEEDED);
            }
        }

        Comment comment = CommentConverter.toComment(user, post, request, parent);

        // 댓글 알림
        notiCommandService.commentNotification(comment);

        return commentRepository.save(comment);
    }

    @Override
    @Transactional
    public void deleteComment(Long postId, Long commentId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.POST_NOT_FOUND));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.COMMENT_NOT_FOUND));

        User user = securityUtil.getCurrentUser();
        Long userId = user.getId();

        // 본인의 댓글이 아닐 경우, 댓글 삭제 불가능
        if (!comment.getUser().getId().equals(userId)) {
            throw new GeneralException(ErrorStatus.COMMENT_NOT_MINE);
        }

        commentRepository.delete(comment);
    }
}

