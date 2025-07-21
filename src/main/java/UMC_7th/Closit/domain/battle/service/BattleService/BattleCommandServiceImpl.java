package UMC_7th.Closit.domain.battle.service.BattleService;

import UMC_7th.Closit.domain.battle.converter.BattleMapper;
import UMC_7th.Closit.domain.battle.dto.BattleDTO.request.ChallengeBattleRequest;
import UMC_7th.Closit.domain.battle.dto.BattleDTO.request.CreateBattleRequest;
import UMC_7th.Closit.domain.battle.dto.BattleDTO.request.DecideChallengeRequest;
import UMC_7th.Closit.domain.battle.dto.BattleDTO.request.VoteBattleRequest;
import UMC_7th.Closit.domain.battle.entity.*;
import UMC_7th.Closit.domain.battle.entity.enums.BattleStatus;
import UMC_7th.Closit.domain.battle.exception.BattleErrorStatus;
import UMC_7th.Closit.domain.battle.repository.BattleRepository;
import UMC_7th.Closit.domain.battle.repository.ChallengeBattleRepository;
import UMC_7th.Closit.domain.battle.repository.VoteRepository;
import UMC_7th.Closit.domain.battle.validator.BattleValidator;
import UMC_7th.Closit.domain.notification.service.NotiCommandService;
import UMC_7th.Closit.domain.post.entity.Post;
import UMC_7th.Closit.domain.post.repository.PostRepository;
import UMC_7th.Closit.domain.user.entity.User;
import UMC_7th.Closit.domain.user.repository.UserRepository;
import UMC_7th.Closit.global.apiPayload.code.status.ErrorStatus;
import UMC_7th.Closit.global.apiPayload.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BattleCommandServiceImpl implements BattleCommandService {

    private final ChallengeBattleRepository challengeBattleRepository;
    private final BattleRepository battleRepository;
    private final PostRepository postRepository;
    private final VoteRepository voteRepository;
    private final UserRepository userRepository;
    private final NotiCommandService notiCommandService;
    private final BattleValidator battleValidator;

    @Override
    public Battle createBattle (Long userId, CreateBattleRequest request) { // 배틀 생성
        Post post = postRepository.findById(request.postId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.POST_NOT_FOUND));

        Battle battle = BattleMapper.toBattle(post, request);

        // 본인의 게시글이 아닐 경우, 배틀 게시글로 업로드 불가능
        if (!post.getUser().getId().equals(userId)) {
            throw new GeneralException(BattleErrorStatus.POST_UNAUTHORIZED_ACCESS);
        }

        return battleRepository.save(battle);
    }

    @Override
    public ChallengeBattle challengeBattle (Long userId, Long battleId, ChallengeBattleRequest request) { // 배틀 신청
        Battle battle = battleRepository.findById(battleId)
                .orElseThrow(() -> new GeneralException(BattleErrorStatus.BATTLE_NOT_FOUND));

        Post post = postRepository.findById(request.postId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.POST_NOT_FOUND));

        battleValidator.validateChallengeBattle(battle, post, userId, request.postId());

        ChallengeBattle challengeBattle = BattleMapper.toChallengeBattle(battle, post);

        notiCommandService.challengeBattleNotification(challengeBattle);

        return challengeBattleRepository.save(challengeBattle);
    }

    @Override
    public Battle acceptChallenge (Long userId, Long battleId, DecideChallengeRequest request) { // 배틀 수락
        Battle battle = battleRepository.findById(battleId)
                .orElseThrow(() -> new GeneralException(BattleErrorStatus.BATTLE_NOT_FOUND));

        ChallengeBattle challengeBattle = challengeBattleRepository.findById(request.challengeBattleId())
                .orElseThrow(() -> new GeneralException(BattleErrorStatus.CHALLENGE_BATTLE_NOT_FOUND));

        challengeBattleRepository.updateChallengeStatus(battle, challengeBattle.getId());

        battleValidator.validateChallengeBattleStatus(userId, battle);

        battle.acceptChallenge(challengeBattle.getPost(), LocalDateTime.now().plusHours(72));

        return battleRepository.save(battle);
    }

    @Override
    public Battle rejectChallenge (Long userId, Long battleId, DecideChallengeRequest request) { // 배틀 거절
        Battle battle = battleRepository.findById(battleId)
                .orElseThrow(() -> new GeneralException(BattleErrorStatus.BATTLE_NOT_FOUND));

        ChallengeBattle challengeBattle = challengeBattleRepository.findById(request.challengeBattleId())
                .orElseThrow(() -> new GeneralException(BattleErrorStatus.CHALLENGE_BATTLE_NOT_FOUND));

        battleValidator.validateChallengeBattleStatus(userId, battle);

        challengeBattle.rejectBattle();

        return battle;
    }

    @Override
    public Vote voteBattle (Long userId, Long battleId, VoteBattleRequest request) { // 배틀 투표
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        Battle battle = battleRepository.findById(battleId)
                .orElseThrow(() -> new GeneralException(BattleErrorStatus.BATTLE_NOT_FOUND));

        postRepository.findById(request.postId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.POST_NOT_FOUND));

        battleValidator.validateVoteBattle(battle, userId, request);

        Vote vote = BattleMapper.toVote(user, request);

        if (battle.getPost1().getId().equals(vote.getVotedPostId())) { // 첫 번째 게시글에 투표
            battleRepository.incrementFirstVotingCnt(battleId);
        } else if (battle.getPost2().getId().equals(vote.getVotedPostId())) { // 두 번째 게시글에 투표
            battleRepository.incrementSecondVotingCnt(battleId);
        }
        Battle updatedBattle = battleRepository.findById(battleId)
                .orElseThrow(() -> new GeneralException(BattleErrorStatus.BATTLE_NOT_FOUND));

        battle.updateVotingCnt(updatedBattle.getFirstVotingCnt(), updatedBattle.getSecondVotingCnt());

        vote.voteBattle(user, battle, request.postId());

        return voteRepository.save(vote);
    }

    @Override
    public void completeBattle() {
        int pageSize = 100;
        Pageable pageable = PageRequest.of(0, pageSize);
        boolean hasNext = true;

        while (hasNext) {
            Slice<Battle> battleList = battleRepository.findByBattleStatusAndDeadlineIsNotNullAndDeadlineBefore(
                    BattleStatus.ACTIVE, LocalDateTime.now(), pageable
            );
            List<Battle> updateBattleList = battleList.getContent();
            updateBattleList.forEach(Battle::completeBattle);

            battleRepository.saveAll(updateBattleList);

            hasNext = battleList.hasNext();
            pageable = battleList.nextPageable();
        }
    }

    @Override
    public void deleteBattle (Long userId, Long battleId) {
        Battle battle = battleRepository.findById(battleId)
                .orElseThrow(() -> new GeneralException(BattleErrorStatus.BATTLE_NOT_FOUND));

        // 배틀 게시글을 생성한 이가 아닐 경우, 삭제 불가능
        if (!battle.getPost1().getUser().getId().equals(userId)) {
            throw new GeneralException(BattleErrorStatus.POST_UNAUTHORIZED_ACCESS);
        }

        battleRepository.delete(battle);
    }
}
