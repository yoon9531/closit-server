package UMC_7th.Closit.domain.battle.converter;

import UMC_7th.Closit.domain.battle.dto.BattleDTO.BattleResponseDTO;
import UMC_7th.Closit.domain.battle.dto.BattleDTO.request.CreateBattleRequest;
import UMC_7th.Closit.domain.battle.dto.BattleDTO.request.VoteBattleRequest;
import UMC_7th.Closit.domain.battle.dto.BattleDTO.response.GetChallengeBattleResponse;
import UMC_7th.Closit.domain.battle.entity.*;
import UMC_7th.Closit.domain.battle.entity.enums.BattleStatus;
import UMC_7th.Closit.domain.battle.entity.enums.ChallengeStatus;
import UMC_7th.Closit.domain.post.entity.Post;
import UMC_7th.Closit.domain.user.entity.User;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.stream.Collectors;

public class BattleMapper {

    public static Battle toBattle(Post post, CreateBattleRequest request) { // 배틀 생성
        return Battle.builder()
                .post1(post)
                .title(request.title())
                .description(request.description())
                .battleStatus(BattleStatus.INACTIVE)
                .build();
    }

    public static ChallengeBattle toChallengeBattle(Battle battle, Post post) { // 배틀 신청
        return ChallengeBattle.builder()
                .battle(battle)
                .post(post)
                .challengeStatus(ChallengeStatus.PENDING)
                .build();
    }

    public static Vote toVote(User user, VoteBattleRequest request) { // 배틀 투표
        return Vote.builder()
                .user(user)
                .votedPostId(request.postId())
                .build();
    }

    public static BattleResponseDTO.BattlePreviewDTO battlePreviewDTO(Battle battle) { // 배틀 게시글 목록 조회
        return BattleResponseDTO.BattlePreviewDTO.builder()
                .battleId(battle.getId())
                .isLiked(!battle.getBattleLikesList().isEmpty())
                .likeCount(battle.getLikeCount())
                .title(battle.getTitle())
                .firstClositId(battle.getPost1().getUser().getClositId())
                .firstProfileImage(battle.getPost1().getUser().getProfileImage())
                .firstPostId(battle.getPost1().getId())
                .firstPostFrontImage(battle.getPost1().getFrontImage())
                .firstPostBackImage(battle.getPost1().getBackImage())
                .firstVotingCnt(battle.getFirstVotingCnt())
                .secondClositId(battle.getPost2().getUser().getClositId())
                .secondProfileImage(battle.getPost2().getUser().getProfileImage())
                .secondPostId(battle.getPost2().getId())
                .secondPostFrontImage(battle.getPost2().getFrontImage())
                .secondPostBackImage(battle.getPost2().getBackImage())
                .secondVotingCnt(battle.getSecondVotingCnt())
                .build();
    }

    public static BattleResponseDTO.BattlePreviewListDTO battlePreviewListDTO(Slice<Battle> battleList) {
        List<BattleResponseDTO.BattlePreviewDTO> battlePreviewDTOList = battleList.stream()
                .map(BattleMapper::battlePreviewDTO).collect(Collectors.toList());

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
                .map(BattleMapper::challengeBattlePreviewDTO).collect(Collectors.toList());

        return BattleResponseDTO.ChallengeBattlePreviewListDTO.builder()
                .challengeBattlePreviewList(challengeBattlePreviewDTOList)
                .listSize(challengeBattlePreviewDTOList.size())
                .isFirst(challengeBattleList.isFirst())
                .isLast(challengeBattleList.isLast())
                .hasNext(challengeBattleList.hasNext())
                .build();
    }
}