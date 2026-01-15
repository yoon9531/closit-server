package UMC_7th.Closit.domain.battle.service;

import UMC_7th.Closit.domain.battle.converter.BattleConverter;
import UMC_7th.Closit.domain.battle.dto.BattleRequestDTO;
import UMC_7th.Closit.domain.battle.entity.*;
import UMC_7th.Closit.domain.battle.entity.enums.BattleStatus;
import UMC_7th.Closit.domain.battle.repository.BattleRepository;
import UMC_7th.Closit.domain.battle.repository.ChallengeBattleRepository;
import UMC_7th.Closit.domain.battle.repository.VoteRepository;
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

    @Override
    public Battle createBattle (Long userId, BattleRequestDTO.CreateBattleDTO request) { // 배틀 생성
        Post post = postRepository.findById(request.getPostId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.POST_NOT_FOUND));

        Battle battle = BattleConverter.toBattle(post, request);

        // 본인의 게시글이 아닐 경우, 배틀 게시글로 업로드 불가능
        if (!post.getUser().getId().equals(userId)) {
            throw new GeneralException(ErrorStatus.POST_UNAUTHORIZED_ACCESS);
        }

        return battleRepository.save(battle);
    }

    @Override
    public ChallengeBattle challengeBattle (Long userId, Long battleId, BattleRequestDTO.ChallengeBattleDTO request) { // 배틀 신청
        Battle battle = battleRepository.findById(battleId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.BATTLE_NOT_FOUND));

        Post post = postRepository.findById(request.getPostId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.POST_NOT_FOUND));

        validateChallengeBattle(battle, post, userId, request.getPostId());

        ChallengeBattle challengeBattle = BattleConverter.toChallengeBattle(battle, post);

        notiCommandService.challengeBattleNotification(challengeBattle);

        return challengeBattleRepository.save(challengeBattle);
    }

    @Override
    public Battle acceptChallenge (Long userId, Long battleId, BattleRequestDTO.ChallengeDecisionDTO request) { // 배틀 수락
        Battle battle = battleRepository.findById(battleId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.BATTLE_NOT_FOUND));

        ChallengeBattle challengeBattle = challengeBattleRepository.findById(request.getChallengeBattleId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.CHALLENGE_BATTLE_NOT_FOUND));

        challengeBattleRepository.updateChallengeStatus(battle, challengeBattle.getId());

        // Post1 게시글 작성자만 수락 가능
        if (!battle.getPost1().getUser().getId().equals(userId)) {
            throw new GeneralException(ErrorStatus.BATTLE_UNAUTHORIZED_ACCESS);
        }
        battle.acceptChallenge(challengeBattle.getPost(), LocalDateTime.now().plusHours(72));

        return battleRepository.save(battle);
    }

    @Override
    public Battle rejectChallenge (Long userId, Long battleId, BattleRequestDTO.ChallengeDecisionDTO request) { // 배틀 거절
        Battle battle = battleRepository.findById(battleId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.BATTLE_NOT_FOUND));

        ChallengeBattle challengeBattle = challengeBattleRepository.findById(request.getChallengeBattleId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.CHALLENGE_BATTLE_NOT_FOUND));

        // Post1 게시글 작성자만 거절 가능
        if (!battle.getPost1().getUser().getId().equals(userId)) {
            throw new GeneralException(ErrorStatus.BATTLE_UNAUTHORIZED_ACCESS);
        }
        challengeBattle.rejectBattle();

        return battle;
    }

    @Override
    public Vote voteBattle (Long userId, Long battleId, BattleRequestDTO.VoteBattleDTO request) { // 배틀 투표
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        Battle battle = battleRepository.findById(battleId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.BATTLE_NOT_FOUND));

        postRepository.findById(request.getPostId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.POST_NOT_FOUND));

        validateVoteBattle(battle, userId, request);

        Vote vote = BattleConverter.toVote(user, request);

        if (battle.getPost1().getId().equals(vote.getVotedPostId())) { // 첫 번째 게시글에 투표
            battleRepository.incrementFirstVotingCnt(battleId);
        } else if (battle.getPost2().getId().equals(vote.getVotedPostId())) { // 두 번째 게시글에 투표
            battleRepository.incrementSecondVotingCnt(battleId);
        }
        Battle updatedBattle = battleRepository.findById(battleId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.BATTLE_NOT_FOUND));

        battle.updateVotingCnt(updatedBattle.getFirstVotingCnt(), updatedBattle.getSecondVotingCnt());

        vote.voteBattle(user, battle, request.getPostId());

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
                .orElseThrow(() -> new GeneralException(ErrorStatus.BATTLE_NOT_FOUND));

        // 배틀 게시글을 생성한 이가 아닐 경우, 삭제 불가능
        if (!battle.getPost1().getUser().getId().equals(userId)) {
            throw new GeneralException(ErrorStatus.POST_UNAUTHORIZED_ACCESS);
        }

        battleRepository.delete(battle);
    }

    private void validateChallengeBattle(Battle battle, Post post, Long userId, Long request) {
        // 동일한 게시글로 배틀 불가능
        if (request.equals(battle.getPost1().getId())) {
            throw new GeneralException(ErrorStatus.BATTLE_NOT_CHALLENGE);
        }

        // 배틀 형성 완료 -> 신청 불가능
        if (battle.getPost2() != null) {
            throw new GeneralException(ErrorStatus.BATTLE_ALREADY_EXIST);
        }

        // 내 게시글에 배틀 신청 불가능
        if (battle.getPost1().getUser().getId().equals(userId)) {
            throw new GeneralException(ErrorStatus.POST_NOT_APPLY);
        }

        // 본인의 게시글일 경우, 배틀 신청 불가능
        if (!post.getUser().getId().equals(userId)) {
            throw new GeneralException(ErrorStatus.POST_UNAUTHORIZED_ACCESS);
        }

        // Status = INACTIVE일 경우에만 배틀 신청 가능
        if (battle.getBattleStatus().equals(BattleStatus.INACTIVE)) {
            battle.challengeBattle();
        }
    }

    private void validateVoteBattle(Battle battle, Long userId, BattleRequestDTO.VoteBattleDTO request) {
        // 배틀이 아닌 게시글에 투표 불가능
        if (isInvalidBattlePost(battle, request.getPostId())) {
            throw new GeneralException(ErrorStatus.POST_NOT_BATTLE);
        }

        // 이미 투표한 곳에 중복 투표 방지
        boolean alreadyVoted = voteRepository.existsByBattleIdAndUserId(battle.getId(), userId);
        if (alreadyVoted) {
            throw new GeneralException(ErrorStatus.VOTE_ALREADY_EXIST);
        }

        // 마감 기한 후 투표 방지
        if (battle.availableVote()) {
            throw new GeneralException(ErrorStatus.VOTE_EXPIRED);
        }

        // 챌린지 게시글 투표 방지
        if (battle.getPost2() == null) {
            throw new GeneralException(ErrorStatus.POST_IS_CHALLENGE);
        }
    }

    private boolean isInvalidBattlePost(Battle battle, Long postId) {
        return !battle.getPost1().getId().equals(postId) && !battle.getPost2().getId().equals(postId);
    }
}
