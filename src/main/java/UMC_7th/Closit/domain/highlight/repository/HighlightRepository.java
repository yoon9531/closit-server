package UMC_7th.Closit.domain.highlight.repository;

import UMC_7th.Closit.domain.highlight.entity.Highlight;
import UMC_7th.Closit.domain.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface HighlightRepository extends JpaRepository<Highlight, Long> {

    Optional<Highlight> findByPostId(Long postId);

    @Query("SELECT h FROM Highlight h LEFT JOIN FETCH h.post WHERE h.id = :highlightId")
    Optional<Highlight> findByIdWithPost(Long highlightId);

    Slice<Highlight> findAllByUser(User user, Pageable pageable);
}
