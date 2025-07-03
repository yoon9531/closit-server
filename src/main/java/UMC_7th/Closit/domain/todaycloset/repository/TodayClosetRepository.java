package UMC_7th.Closit.domain.todaycloset.repository;

import UMC_7th.Closit.domain.post.entity.Post;
import UMC_7th.Closit.domain.todaycloset.entity.TodayCloset;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface TodayClosetRepository extends JpaRepository<TodayCloset, Long> {
    Page<TodayCloset> findAll(Pageable pageable);

    boolean existsByPost(Post post);
    @Query("SELECT tc FROM TodayCloset tc JOIN tc.post p ORDER BY p.view DESC")
    Slice<TodayCloset> findAllOrderByPostView(Pageable pageable);
    @Query("SELECT tc FROM TodayCloset tc ORDER BY tc.createdAt DESC")
    Slice<TodayCloset> findAllOrderByCreatedAt(Pageable pageable);

    // 조회수 순 정렬 (24시간 이내)
    @Query("""
    SELECT tc 
    FROM TodayCloset tc 
    JOIN tc.post p 
    WHERE tc.createdAt BETWEEN :start AND :end 
    ORDER BY p.view DESC
    """)
    Slice<TodayCloset> findTodayClosetsOrderByPostView(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            Pageable pageable
    );

    // 최신순 정렬 (24시간 이내)
    @Query("""
    SELECT tc 
    FROM TodayCloset tc 
    WHERE tc.createdAt BETWEEN :start AND :end 
    ORDER BY tc.createdAt DESC
    """)
    Slice<TodayCloset> findTodayClosetsByCreatedAtBetween(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            Pageable pageable
    );
}
