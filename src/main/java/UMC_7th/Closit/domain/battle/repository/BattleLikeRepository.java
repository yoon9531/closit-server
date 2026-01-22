package UMC_7th.Closit.domain.battle.repository;

import UMC_7th.Closit.domain.battle.entity.BattleLike;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BattleLikeRepository extends JpaRepository<BattleLike, Long> {
   boolean existsBattleLikeByBattleIdAndUserId(Long battleId, Long userId); // 배틀 좋아요 생성
   Slice<BattleLike> findAllByBattleId(Long battleId, Pageable pageable);
   Optional<BattleLike> findByUserIdAndBattleId(Long userId, Long battleId); // 배틀 좋아요 삭제
}