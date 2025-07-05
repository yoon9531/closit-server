package UMC_7th.Closit.domain.battle.repository;

import UMC_7th.Closit.domain.battle.entity.Battle;
import UMC_7th.Closit.domain.battle.entity.enums.BattleStatus;
import UMC_7th.Closit.domain.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface BattleRepository extends JpaRepository<Battle,Long> {
    @Modifying
    @Query("UPDATE Battle b " +
            "SET b.likeCount = b.likeCount + 1 " +
            "WHERE b.id = :id")
    void incrementLikeCount(@Param("id") Long id);

    @Modifying
    @Query("UPDATE Battle b " +
            "SET b.likeCount = b.likeCount - 1 " +
            "WHERE b.id = :id " +
            "AND b.likeCount > 0")
    void decrementLikeCount(@Param("id") Long id);

    @Modifying
    @Query("UPDATE Battle b " +
            "SET b.firstVotingCnt = b.firstVotingCnt + 1 " +
            "WHERE b.id = :id")
    void incrementFirstVotingCnt(@Param("id") Long id);

    @Modifying
    @Query("UPDATE Battle b " +
            "SET b.secondVotingCnt = b.secondVotingCnt + 1 " +
            "WHERE b.id = :id")
    void incrementSecondVotingCnt(@Param("id") Long id);

    @Modifying
    @Query("UPDATE Battle b " +
           "SET b.viewCount = b.viewCount + 1 " +
           "WHERE b.id = :id")
    int incrementViewCount(@Param("id") Long id);

    Optional<Battle> findByIdAndPost2IsNotNull(Long battleId);

    Slice<Battle> findByPost2IsNotNullAndBattleStatus(Pageable pageable, BattleStatus battleStatus); // 배틀 게시글 목록 조회
    Slice<Battle> findByPost2IsNullAndPost1UserIdNot(Long userId, Pageable pageable); // 배틀 챌린지 게시글 목록 조회
    @Query("""
        select b from Battle b
        where b.post2 is not null
        and b in (
            select v.battle from Vote v
            where v.user = :user
        )
    """)
    Slice<Battle> findVotedBattlesByUserAndPost2IsNotNull(@Param("user") User user, Pageable pageable); // 내가 투표한 게시글 목록 조회
    Slice<Battle> findByBattleStatusAndDeadlineIsNotNullAndDeadlineBefore(BattleStatus battleStatus, LocalDateTime deadline, Pageable pageable); // 배틀 스케줄러 - 진행 상태 변경
}