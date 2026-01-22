package UMC_7th.Closit.domain.post.repository;

import UMC_7th.Closit.domain.post.entity.ItemTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ItemTagRepository extends JpaRepository<ItemTag, Long> {
    Optional<ItemTag> findByContent(String content);
}
