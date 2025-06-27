package UMC_7th.Closit.domain.post.service;

import UMC_7th.Closit.domain.post.entity.Comment;
import UMC_7th.Closit.domain.post.entity.Post;
import UMC_7th.Closit.domain.post.repository.CommentRepository;
import UMC_7th.Closit.domain.post.repository.PostRepository;
import UMC_7th.Closit.global.apiPayload.code.status.ErrorStatus;
import UMC_7th.Closit.global.apiPayload.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentQueryServiceImpl implements CommentQueryService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Override
    @Transactional(readOnly = true)
    public Slice<Comment> getCommentList(Long postId, Integer page) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.POST_NOT_FOUND));

        Pageable pageable = PageRequest.of(page, 10);

        return commentRepository.findAllByPostIdAndParentIsNull(postId, pageable);
    }
}

