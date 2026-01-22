package UMC_7th.Closit.domain.battle.repository;

import UMC_7th.Closit.domain.battle.entity.BattleComment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BattleCommentRepository extends JpaRepository<BattleComment, Long> {
    Slice<BattleComment> findAllByBattleId(Long battleId, Pageable pageable);
}
