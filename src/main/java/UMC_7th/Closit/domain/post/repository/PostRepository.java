package UMC_7th.Closit.domain.post.repository;

import UMC_7th.Closit.domain.post.entity.Post;
import UMC_7th.Closit.domain.post.entity.enums.Visibility;
import UMC_7th.Closit.domain.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findById(Long postId);

    @Query("SELECT p FROM Post p WHERE p.user IN :users ORDER BY p.createdAt DESC")
    Slice<Post> findByUsers(List<User> users, Pageable pageable);

    @Query("SELECT p FROM Post p JOIN p.postHashtagList pht WHERE p.user IN :users AND pht.hashtag.id = :hashtagId ORDER BY p.createdAt DESC")
    Slice<Post> findByUsersAndHashtagId(List<User> users, Long hashtagId, Pageable pageable);

    @Query("SELECT p FROM Post p JOIN p.postHashtagList pht WHERE pht.hashtag.id = :hashtagId")
    Slice<Post> findByHashtagId(Long hashtagId, Pageable pageable);

    @Query("SELECT p FROM Post p JOIN p.postItemTagList pit WHERE pit.itemTag.id = :itemTagId")
    Slice<Post> findByItemTagId(@Param("itemTagId") Long itemTagId, Pageable pageable);

    Slice<Post> findAllByOrderByCreatedAtDesc(Pageable pageable);

    List<Post> findByUserId(Long userId); // 미션 알림

    @Query("SELECT p FROM Post p WHERE p.user.id = :userId GROUP BY FUNCTION('DATE', p.createdAt) ORDER BY p.createdAt ASC") // 히스토리 썸네일 조회
    Slice<Post> findFrontImageByUserId(@Param("userId") Long userId, Pageable pageable);

    List<Post> findAllByUserIdAndCreatedAtBetween (Long userId, LocalDateTime start, LocalDateTime end); // 히스토리 게시글 상세 조회 - 하루 내 게시글 탐색

    @Query("SELECT p FROM Post p WHERE p.user.id = :userId GROUP BY FUNCTION('DATE', p.createdAt) ORDER BY p.createdAt ASC")
    Slice<Post> findPointColorByUserId(@Param("userId") Long userId, Pageable pageable); // 히스토리 포인트 색상 썸네일 조회

    @Query("SELECT p FROM Post p WHERE p.user.clositId = :clositId AND p.createdAt >= :week ORDER BY p.createdAt DESC")
    Slice<Post> findRecentPostList(@Param("clositId") String clositId, @Param("week") LocalDateTime week, Pageable pageable); // 특정 사용자의 최근 게시글 조회

    @Query("SELECT p FROM Post p WHERE p.visibility = :visibility")
    Slice<Post> findByVisibility(@Param("visibility") Visibility visibility, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.visibility = :visibility AND p.user IN :users")
    Slice<Post> findByVisibilityAndUserIn(@Param("visibility") Visibility visibility, @Param("users") List<User> users, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.visibility = :visibility AND p.user = :user")
    Slice<Post> findByVisibilityAndUser(@Param("visibility") Visibility visibility, @Param("user") User user, Pageable pageable);

    @Modifying
    @Query("UPDATE Post p SET p.likes = p.likes + 1 WHERE p.id = :id")
    void incrementLikeCount(@Param("id") Long id);
}