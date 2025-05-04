package UMC_7th.Closit.domain.todaycloset.repository;

import UMC_7th.Closit.domain.post.entity.Post;
import UMC_7th.Closit.domain.todaycloset.entity.TodayCloset;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodayClosetRepository extends JpaRepository<TodayCloset, Long> {
    Page<TodayCloset> findAll(Pageable pageable);
    boolean existsByPost(Post post);
}
