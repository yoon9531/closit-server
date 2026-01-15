package UMC_7th.Closit.domain.battle.service;

import UMC_7th.Closit.domain.battle.entity.BattleComment;
import org.springframework.data.domain.Slice;

public interface BattleCmtQueryService {
    Slice<BattleComment> getBattleCommentList(Long battleId, Integer page); // 배틀 댓글 조회
}
