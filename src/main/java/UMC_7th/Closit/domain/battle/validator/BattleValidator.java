package UMC_7th.Closit.domain.battle.validator;

import UMC_7th.Closit.domain.battle.dto.BattleDTO.BattleRequestDTO;
import UMC_7th.Closit.domain.battle.entity.Battle;
import UMC_7th.Closit.domain.battle.entity.enums.BattleStatus;
import UMC_7th.Closit.domain.battle.exception.BattleErrorStatus;
import UMC_7th.Closit.domain.battle.repository.VoteRepository;
import UMC_7th.Closit.domain.post.entity.Post;
import UMC_7th.Closit.global.apiPayload.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BattleValidator {

    private final VoteRepository voteRepository;

    public void validateChallengeBattleStatus(Long userId, Battle battle) {
        // Post1 게시글 작성자만 수락 가능
        if (!battle.getPost1().getUser().getId().equals(userId)) {
            throw new GeneralException(BattleErrorStatus.BATTLE_UNAUTHORIZED_ACCESS);
        }
    }

    public void validateChallengeBattle(Battle battle, Post post, Long userId, Long request) {
        // 동일한 게시글로 배틀 불가능
        if (request.equals(battle.getPost1().getId())) {
            throw new GeneralException(BattleErrorStatus.BATTLE_NOT_CHALLENGE);
        }

        // 배틀 형성 완료 -> 신청 불가능
        if (battle.getPost2() != null) {
            throw new GeneralException(BattleErrorStatus.BATTLE_ALREADY_EXIST);
        }

        // 내 게시글에 배틀 신청 불가능
        if (battle.getPost1().getUser().getId().equals(userId)) {
            throw new GeneralException(BattleErrorStatus.POST_NOT_APPLY);
        }

        // 본인의 게시글일 경우, 배틀 신청 불가능
        if (!post.getUser().getId().equals(userId)) {
            throw new GeneralException(BattleErrorStatus.POST_UNAUTHORIZED_ACCESS);
        }

        // Status = INACTIVE일 경우에만 배틀 신청 가능
        if (battle.getBattleStatus().equals(BattleStatus.INACTIVE)) {
            battle.challengeBattle();
        }
    }

    public void validateVoteBattle(Battle battle, Long userId, BattleRequestDTO.VoteBattleDTO request) {
        // 배틀이 아닌 게시글에 투표 불가능
        if (isInvalidBattlePost(battle, request.getPostId())) {
            throw new GeneralException(BattleErrorStatus.POST_NOT_BATTLE);
        }

        // 이미 투표한 곳에 중복 투표 방지
        boolean alreadyVoted = voteRepository.existsByBattleIdAndUserId(battle.getId(), userId);
        if (alreadyVoted) {
            throw new GeneralException(BattleErrorStatus.VOTE_ALREADY_EXIST);
        }

        // 마감 기한 후 투표 방지
        if (battle.availableVote()) {
            throw new GeneralException(BattleErrorStatus.VOTE_EXPIRED);
        }

        // 챌린지 게시글 투표 방지
        if (battle.getPost2() == null) {
            throw new GeneralException(BattleErrorStatus.POST_IS_CHALLENGE);
        }
    }

    private boolean isInvalidBattlePost(Battle battle, Long postId) {
        return !battle.getPost1().getId().equals(postId) && !battle.getPost2().getId().equals(postId);
    }
}
