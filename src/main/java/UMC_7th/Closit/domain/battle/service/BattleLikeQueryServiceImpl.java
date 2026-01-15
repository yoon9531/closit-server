package UMC_7th.Closit.domain.battle.service;

import UMC_7th.Closit.domain.battle.entity.Battle;
import UMC_7th.Closit.domain.battle.entity.BattleLike;
import UMC_7th.Closit.domain.battle.repository.BattleLikeRepository;
import UMC_7th.Closit.domain.battle.repository.BattleRepository;
import UMC_7th.Closit.global.apiPayload.code.status.ErrorStatus;
import UMC_7th.Closit.global.apiPayload.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BattleLikeQueryServiceImpl implements BattleLikeQueryService {

    private final BattleRepository battleRepository;
    private final BattleLikeRepository battleLikeRepository;

    @Override

    public Slice<BattleLike> getBattleLikeList(Long battleId, Integer page) { // 배틀 좋아요 조회
        Pageable pageable = PageRequest.of(page, 10);

        Battle battle = battleRepository.findById(battleId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.BATTLE_NOT_FOUND));

        Slice<BattleLike> battleLikeList = battleLikeRepository.findAllByBattleId(battleId, pageable);

        return battleLikeList;
    }
}
