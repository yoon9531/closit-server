package UMC_7th.Closit.domain.follow.repository;

import UMC_7th.Closit.domain.follow.entity.Follow;
import UMC_7th.Closit.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {
    List<Follow> findBySender(User sender);

    // senderId와 receiverId로 특정 팔로우 관계 조회
    Optional<Follow> findBySenderClositIdAndReceiverClositId(String senderClositId, String receiverClositId);

    // 특정 유저를 팔로우하는 사람 목록 조회 (팔로워)
    Slice<Follow> findByReceiver(User user, Pageable pageable);

    // 특정 유저가 팔로우하는 사람 목록 조회 (팔로잉)
    Slice<Follow> findBySender(User user, Pageable pageable);

    // 특정 유저의 전체 팔로워 수 조회
    @Query("SELECT COUNT(f) FROM Follow f WHERE f.receiver = :user")
    long countByReceiver(@Param("user") User user);

    // 특정 유저의 전체 팔로잉 수 조회
    @Query("SELECT COUNT(f) FROM Follow f WHERE f.sender = :user")
    long countBySender(@Param("user") User user);

    Follow findBySenderAndReceiver (User sender, User receiver);
}
