package UMC_7th.Closit.domain.post.repository;

import UMC_7th.Closit.domain.post.entity.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Slice<Comment> findAllByPostId(Long postId, Pageable pageable);
}

