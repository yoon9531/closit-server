package UMC_7th.Closit.domain.post.service;

import UMC_7th.Closit.domain.post.entity.Comment;
import org.springframework.data.domain.Slice;

public interface CommentQueryService {
    Slice<Comment> getCommentList(Long post_id, Integer page);
}
