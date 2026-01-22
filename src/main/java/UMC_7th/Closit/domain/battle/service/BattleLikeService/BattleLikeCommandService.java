package UMC_7th.Closit.domain.battle.service.BattleLikeService;

import UMC_7th.Closit.domain.battle.entity.BattleLike;

public interface BattleLikeCommandService {
    BattleLike createBattleLike(Long userId, Long battleId); // 배틀 좋아요 생성
    void deleteBattleLike(Long userId, Long battleId); // 배틀 좋아요 삭제
}