package UMC_7th.Closit.domain.post.repository;

import UMC_7th.Closit.domain.post.entity.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @EntityGraph(attributePaths = {"children", "children.user"})
    Slice<Comment> findAllByPostIdAndParentIsNull(Long postId, Pageable pageable);
}

