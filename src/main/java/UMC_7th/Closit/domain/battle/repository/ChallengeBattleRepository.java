package UMC_7th.Closit.domain.battle.repository;

import UMC_7th.Closit.domain.battle.entity.Battle;
import UMC_7th.Closit.domain.battle.entity.ChallengeBattle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChallengeBattleRepository extends JpaRepository<ChallengeBattle, Long> {
    @Modifying
    @Query("UPDATE ChallengeBattle cb SET cb.challengeStatus = " +
           "CASE WHEN cb.id = :challengeBattleId THEN 'ACCEPTED' ELSE 'REJECTED' END " +
           "WHERE cb.battle = :battle")
    int updateChallengeStatus(@Param("battle") Battle battle, @Param("challengeBattleId") Long challengeBattleId);

    ChallengeBattle findByIdAndBattleId(Long challengeBattleId, Long battleId);
}
