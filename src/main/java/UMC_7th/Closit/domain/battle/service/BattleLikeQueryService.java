package UMC_7th.Closit.domain.battle.service;

import UMC_7th.Closit.domain.battle.entity.BattleLike;
import org.springframework.data.domain.Slice;

public interface BattleLikeQueryService {
    Slice<BattleLike> getBattleLikeList(Long battleId, Integer page); // 배틀 좋아요 조회
}
