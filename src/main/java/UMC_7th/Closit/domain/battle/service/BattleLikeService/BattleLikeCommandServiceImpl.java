package UMC_7th.Closit.domain.battle.service.BattleLikeService;

import UMC_7th.Closit.domain.battle.converter.BattleLikeConverter;
import UMC_7th.Closit.domain.battle.entity.Battle;
import UMC_7th.Closit.domain.battle.entity.BattleLike;
import UMC_7th.Closit.domain.battle.repository.BattleLikeRepository;
import UMC_7th.Closit.domain.battle.repository.BattleRepository;
import UMC_7th.Closit.domain.user.entity.User;
import UMC_7th.Closit.domain.user.repository.UserRepository;
import UMC_7th.Closit.global.apiPayload.code.status.ErrorStatus;
import UMC_7th.Closit.global.apiPayload.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class BattleLikeCommandServiceImpl implements BattleLikeCommandService {

    private final UserRepository userRepository;
    private final BattleRepository battleRepository;
    private final BattleLikeRepository battleLikeRepository;

    @Override
    public BattleLike createBattleLike (Long userId, Long battleId) { // 배틀 좋아요 생성
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));
        Battle battle = battleRepository.findById(battleId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.BATTLE_NOT_FOUND));

        BattleLike battleLike = BattleLikeConverter.toBattleLike(user, battle);

        // 배틀 좋아요 중복 생성 방지
        boolean battleLikeExist = battleLikeRepository.existsBattleLikeByBattleIdAndUserId(battleId, userId);
        if (battleLikeExist) {
            throw new GeneralException(ErrorStatus.BATTLE_LIKES_ALREADY_EXIST);
        }

        battle.increaseLikeCount();

        return battleLikeRepository.save(battleLike);
    }

    @Override
    public void deleteBattleLike (Long userId, Long battleId) { // 배틀 좋아요 삭제
        Battle battle = battleRepository.findById(battleId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.BATTLE_NOT_FOUND));

        BattleLike battleLike = battleLikeRepository.findByUserIdAndBattleId(userId, battleId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.BATTLE_LIKES_NOT_FOUND));

        battle.decreaseLikeCount();

        battleLikeRepository.delete(battleLike);
    }
}