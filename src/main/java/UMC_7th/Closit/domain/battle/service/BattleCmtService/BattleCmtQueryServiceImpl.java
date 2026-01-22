package UMC_7th.Closit.domain.battle.service.BattleCmtService;

import UMC_7th.Closit.domain.battle.entity.BattleComment;
import UMC_7th.Closit.domain.battle.exception.BattleErrorStatus;
import UMC_7th.Closit.domain.battle.repository.BattleCommentRepository;
import UMC_7th.Closit.domain.battle.repository.BattleRepository;
import UMC_7th.Closit.global.apiPayload.code.status.ErrorStatus;
import UMC_7th.Closit.global.apiPayload.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BattleCmtQueryServiceImpl implements BattleCmtQueryService {

    private final BattleRepository battleRepository;
    private final BattleCommentRepository battleCommentRepository;

    @Override
    public Slice<BattleComment> getBattleCommentList (Long battleId, Integer page) { // 배틀 댓글 조회
        battleRepository.findById(battleId)
                .orElseThrow(() -> new GeneralException(BattleErrorStatus.BATTLE_NOT_FOUND));

        return battleCommentRepository.findByBattleIdAndParentBattleCommentIsNullOrderByCreatedAtAsc(battleId, PageRequest.of(page, 10));
    }
}
