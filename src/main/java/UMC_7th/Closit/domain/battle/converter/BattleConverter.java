package UMC_7th.Closit.domain.battle.converter;

import UMC_7th.Closit.domain.battle.dto.BattleDTO.BattleRequestDTO;
import UMC_7th.Closit.domain.battle.dto.BattleDTO.BattleResponseDTO;
import UMC_7th.Closit.domain.battle.entity.*;
import UMC_7th.Closit.domain.post.entity.Post;
import UMC_7th.Closit.domain.user.entity.User;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.stream.Collectors;

public class BattleConverter {

    public static Battle toBattle(Post post, BattleRequestDTO.CreateBattleDTO request) { // 배틀 생성
        return Battle.builder()
                .post1(post)
                .title(request.getTitle())
                .battleStatus(BattleStatus.INACTIVE)
                .build();
    }

    public static BattleResponseDTO.CreateBattleResultDTO createBattleResultDTO(Battle battle) {
        return BattleResponseDTO.CreateBattleResultDTO.builder()
                .battleId(battle.getId())
                .thumbnail(battle.getPost1().getFrontImage())
                .battleStatus(battle.getBattleStatus())
                .createdAt(battle.getCreatedAt())
                .build();
    }

    public static ChallengeBattle toChallengeBattle(Battle battle, Post post) { // 배틀 신청
        return ChallengeBattle.builder()
                .battle(battle)
                .post(post)
                .challengeStatus(ChallengeStatus.PENDING)
                .build();
    }

    public static BattleResponseDTO.ChallengeBattleResultDTO challengeBattleResultDTO(ChallengeBattle challengeBattle) {
        return BattleResponseDTO.ChallengeBattleResultDTO.builder()
                .challengeBattleId(challengeBattle.getId())
                .firstClositId(challengeBattle.getBattle().getPost1().getUser().getClositId())
                .firstPostId(challengeBattle.getBattle().getPost1().getId())
                .firstPostFrontImage(challengeBattle.getBattle().getPost1().getFrontImage())
                .firstPostBackImage(challengeBattle.getBattle().getPost1().getBackImage())
                .secondClositId(challengeBattle.getPost().getUser().getClositId())
                .secondPostId(challengeBattle.getPost().getId())
                .secondPostFrontImage(challengeBattle.getPost().getFrontImage())
                .secondPostBackImage(challengeBattle.getPost().getBackImage())
                .challengeStatus(challengeBattle.getChallengeStatus())
                .createdAt(challengeBattle.getCreatedAt())
                .build();
    }

    public static BattleResponseDTO.ChallengeDecisionDTO challengeDecisionDTO(Battle battle) {
        return BattleResponseDTO.ChallengeDecisionDTO.builder()
                .battleStatus(battle.getBattleStatus())
                .deadline(battle.getDeadline())
                .updatedAt(battle.getUpdatedAt())
                .build();
    }

    public static Vote toVote(User user, BattleRequestDTO.VoteBattleDTO request) { // 배틀 투표
        return Vote.builder()
                .user(user)
                .votedPostId(request.getPostId())
                .build();
    }

    public static BattleResponseDTO.VoteBattleResultDTO voteBattleResultDTO(Vote vote) {
        return BattleResponseDTO.VoteBattleResultDTO.builder()
                .battleId(vote.getBattle().getId())
                .firstClositId(vote.getBattle().getPost1().getUser().getClositId())
                .firstVotingRate(vote.getBattle().getFirstVotingRate())
                .secondClositId(vote.getBattle().getPost2().getUser().getClositId())
                .secondVotingRate(vote.getBattle().getSecondVotingRate())
                .createdAt(vote.getBattle().getCreatedAt())
                .build();
    }

    public static BattleResponseDTO.BattlePreviewDTO battlePreviewDTO(Battle battle) { // 배틀 게시글 목록 조회
        return BattleResponseDTO.BattlePreviewDTO.builder()
                .battleId(battle.getId())
                .isLiked(!battle.getBattleLikesList().isEmpty())
                .title(battle.getTitle())
                .firstClositId(battle.getPost1().getUser().getClositId())
                .firstProfileImage(battle.getPost1().getUser().getProfileImage())
                .firstPostId(battle.getPost1().getId())
                .firstPostFrontImage(battle.getPost1().getFrontImage())
                .firstPostBackImage(battle.getPost1().getBackImage())
                .firstVotingRate(battle.getFirstVotingRate())
                .secondClositId(battle.getPost2().getUser().getClositId())
                .secondProfileImage(battle.getPost2().getUser().getProfileImage())
                .secondPostId(battle.getPost2().getId())
                .secondPostFrontImage(battle.getPost2().getFrontImage())
                .secondPostBackImage(battle.getPost2().getBackImage())
                .secondVotingRate(battle.getSecondVotingRate())
                .build();
    }

    public static BattleResponseDTO.BattlePreviewListDTO battlePreviewListDTO(Slice<Battle> battleList) {
        List<BattleResponseDTO.BattlePreviewDTO> battlePreviewDTOList = battleList.stream()
                .map(BattleConverter::battlePreviewDTO).collect(Collectors.toList());

        return BattleResponseDTO.BattlePreviewListDTO.builder()
                .battlePreviewList(battlePreviewDTOList)
                .listSize(battlePreviewDTOList.size())
                .isFirst(battleList.isFirst())
                .isLast(battleList.isLast())
                .hasNext(battleList.hasNext())
                .build();
    }

    public static BattleResponseDTO.ChallengeBattlePreviewDTO challengeBattlePreviewDTO(Battle battle) { // 배틀 챌린지 게시글 목록 조회
        return BattleResponseDTO.ChallengeBattlePreviewDTO.builder()
                .battleId(battle.getId())
                .firstClositId(battle.getPost1().getUser().getClositId())
                .firstProfileImage(battle.getPost1().getUser().getProfileImage())
                .firstPostId(battle.getPost1().getId())
                .firstPostFrontImage(battle.getPost1().getFrontImage())
                .firstPostBackImage(battle.getPost1().getBackImage())
                .title(battle.getTitle())
                .build();
    }

    public static BattleResponseDTO.ChallengeBattlePreviewListDTO challengeBattlePreviewListDTO(Slice<Battle> challengeBattleList) {
        List<BattleResponseDTO.ChallengeBattlePreviewDTO> challengeBattlePreviewDTOList = challengeBattleList.stream()
                .map(BattleConverter::challengeBattlePreviewDTO).collect(Collectors.toList());

        return BattleResponseDTO.ChallengeBattlePreviewListDTO.builder()
                .challengeBattlePreviewList(challengeBattlePreviewDTOList)
                .listSize(challengeBattlePreviewDTOList.size())
                .isFirst(challengeBattleList.isFirst())
                .isLast(challengeBattleList.isLast())
                .hasNext(challengeBattleList.hasNext())
                .build();
    }
}