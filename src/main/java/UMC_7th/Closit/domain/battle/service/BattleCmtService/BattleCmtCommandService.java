package UMC_7th.Closit.domain.battle.service.BattleCmtService;

import UMC_7th.Closit.domain.battle.dto.BattleCmtDTO.BattleCommentRequestDTO;
import UMC_7th.Closit.domain.battle.entity.BattleComment;

public interface BattleCmtCommandService {
    BattleComment createBattleComment(Long userId, Long battleId, BattleCommentRequestDTO.CreateBattleCommentDTO request); // 배틀 댓글 생성
    void deleteBattleComment(Long userId, Long battleId, Long battleCommentId); // 배틀 댓글 삭제
}