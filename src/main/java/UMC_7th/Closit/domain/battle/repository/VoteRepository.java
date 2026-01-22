package UMC_7th.Closit.domain.battle.repository;

import UMC_7th.Closit.domain.battle.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    boolean existsByBattleIdAndUserId(Long battleId, Long userId); // 배틀 투표, 배틀 게시글 목록 조회
}
