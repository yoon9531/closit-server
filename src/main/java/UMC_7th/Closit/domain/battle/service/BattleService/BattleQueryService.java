package UMC_7th.Closit.domain.battle.service.BattleService;

import UMC_7th.Closit.domain.battle.entity.Battle;
import UMC_7th.Closit.domain.battle.entity.BattleSorting;
import UMC_7th.Closit.domain.battle.entity.BattleStatus;
import org.springframework.data.domain.Slice;

public interface BattleQueryService {

    Slice<Battle> getBattleList(Integer page, BattleSorting battleSorting, BattleStatus battleStatus); // 배틀 게시글 목록 조회
    Slice<Battle> getChallengeBattleList (Integer page); // 배틀 챌린지 게시글 목록 조회 - 최신순
}