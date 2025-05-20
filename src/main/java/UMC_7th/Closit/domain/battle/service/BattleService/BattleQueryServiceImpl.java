package UMC_7th.Closit.domain.battle.service.BattleService;

import UMC_7th.Closit.domain.battle.entity.Battle;
import UMC_7th.Closit.domain.battle.entity.ChallengeBattle;
import UMC_7th.Closit.domain.battle.entity.enums.BattleSorting;
import UMC_7th.Closit.domain.battle.entity.enums.BattleStatus;
import UMC_7th.Closit.domain.battle.repository.BattleRepository;
import UMC_7th.Closit.domain.battle.repository.ChallengeBattleRepository;
import UMC_7th.Closit.domain.battle.repository.VoteRepository;
import UMC_7th.Closit.domain.user.entity.User;
import UMC_7th.Closit.global.apiPayload.code.status.ErrorStatus;
import UMC_7th.Closit.global.apiPayload.exception.GeneralException;
import UMC_7th.Closit.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BattleQueryServiceImpl implements BattleQueryService {

    private final ChallengeBattleRepository challengeBattleRepository;
    private final BattleRepository battleRepository;
    private final VoteRepository voteRepository;
    private final SecurityUtil securityUtil;

    @Override
    @Transactional
    public Battle getBattleDetail(Long battleId) {
        User user = securityUtil.getCurrentUser();

        Battle battle = battleRepository.findByIdAndPost2IsNotNull(battleId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.BATTLE_NOT_FOUND));

        battleRepository.incrementViewCount(battle.getId());

        calculateVotes(battle, user.getId());

        return battle;
    }

    @Override
    public Slice<Battle> getBattleList(Integer page, BattleSorting battleSorting, BattleStatus battleStatus) { // 배틀 게시글 목록 조회
        User user = securityUtil.getCurrentUser();
        Long userId = user.getId();

        Pageable pageable = PageRequest.of(page, 10, battleSorting.getSort());

        // secondPostId가 not null 기준으로 조회
        Slice<Battle> battleList = battleRepository.findByPost2IsNotNullAndBattleStatus(pageable, battleStatus);

        battleList.forEach(battle -> {
            calculateVotes(battle, userId);
        });
        return battleList;
    }

    @Override
    public ChallengeBattle getChallengeBattle(Long battleId, Long challengeBattleId) { // 챌린지 배틀 미리보기
        battleRepository.findById(battleId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.BATTLE_NOT_FOUND));

        challengeBattleRepository.findById(challengeBattleId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.CHALLENGE_BATTLE_NOT_FOUND));

        return challengeBattleRepository.findByIdAndBattleId(challengeBattleId, battleId);
    }

    @Override
    public Slice<Battle> getChallengeBattleList(Integer page) { // 배틀 챌린지 게시글 목록 조회 - 최신순
        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "createdAt"));

        return battleRepository.findByPost2IsNull(pageable);
    }

    @Override
    public Slice<Battle> getMyVotedBattleList(Integer page) { // 내가 투표한 게시글 - 최신순
        User user = securityUtil.getCurrentUser();

        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "createdAt"));

        Slice<Battle> votedBattleList = battleRepository.findVotedBattlesByUserAndPost2IsNotNull(user, pageable);

        votedBattleList.forEach(battle -> {
            calculateVotes(battle, user.getId());
        });

        return votedBattleList;
    }

    private void calculateVotes(Battle battle, Long userId) {
        boolean isVoted = voteRepository.existsByBattleIdAndUserId(battle.getId(), userId);
        if (!isVoted) { // 투표하지 않았으면 해당 배틀 투표 수 null로 표시
            battle.updateVotingCnt(null, null);
        } else { // 투표했을 경우
            Integer firstVotingCnt = battle.getFirstVotingCnt();
            Integer secondVotingCnt = battle.getSecondVotingCnt();
            int totalVoting = firstVotingCnt + secondVotingCnt;

            // 투표 수 비율로 반환
            double firstVotingPercentage = (totalVoting == 0) ? 0.0 : (firstVotingCnt * 100.0) / totalVoting;
            double secondVotingPercentage = (totalVoting == 0) ? 0.0 : (secondVotingCnt * 100.0) / totalVoting;

            battle.updateVotingRate(firstVotingPercentage, secondVotingPercentage);
        }
    }
}