package UMC_7th.Closit.domain.todaycloset.repository;

import UMC_7th.Closit.domain.post.entity.Post;
import UMC_7th.Closit.domain.todaycloset.entity.TodayCloset;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TodayClosetRepository extends JpaRepository<TodayCloset, Long> {
    Page<TodayCloset> findAll(Pageable pageable);

    boolean existsByPost(Post post);
    @Query("SELECT tc FROM TodayCloset tc JOIN tc.post p ORDER BY p.view DESC")
    Slice<TodayCloset> findAllOrderByPostView(Pageable pageable);
    @Query("SELECT tc FROM TodayCloset tc ORDER BY tc.createdAt DESC")
    Slice<TodayCloset> findAllOrderByCreatedAt(Pageable pageable);
}
