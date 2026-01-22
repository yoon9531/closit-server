package UMC_7th.Closit.domain.post.repository;

import UMC_7th.Closit.domain.post.entity.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HashtagRepository extends JpaRepository<Hashtag, Long> {
    Optional<Hashtag> findByContent(String content);
}
