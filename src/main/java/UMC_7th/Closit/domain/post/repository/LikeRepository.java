package UMC_7th.Closit.domain.post.repository;

import UMC_7th.Closit.domain.post.entity.Likes;
import UMC_7th.Closit.domain.post.entity.Post;
import UMC_7th.Closit.domain.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Likes, Long> {
    boolean existsByUserAndPost(User user, Post post);
    
    Optional<Likes> findByUserAndPost(User user, Post post);

    Slice<Likes> findByPost(Post post, Pageable pageable);
}
