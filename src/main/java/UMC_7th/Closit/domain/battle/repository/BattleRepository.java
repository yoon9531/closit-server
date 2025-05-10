package UMC_7th.Closit.domain.battle.repository;

import UMC_7th.Closit.domain.battle.entity.Battle;
import UMC_7th.Closit.domain.battle.entity.BattleStatus;
import UMC_7th.Closit.domain.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface BattleRepository extends JpaRepository<Battle,Long> {
    Slice<Battle> findByPost2IsNotNullAndBattleStatus(Pageable pageable, BattleStatus battleStatus); // 배틀 게시글 목록 조회
    Slice<Battle> findByPost2IsNull(Pageable pageable); // 배틀 챌린지 게시글 목록 조회
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