package UMC_7th.Closit.domain.post.repository;

import UMC_7th.Closit.domain.post.entity.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Slice<Comment> findAllByPostId(Long postId, Pageable pageable);

    @Modifying
    @Query(value = "DELETE FROM comment WHERE user_id = :userId", nativeQuery = true)
    void deleteByUserId(@Param("userId") Long userId);
}