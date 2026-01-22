package UMC_7th.Closit.domain.battle.converter;

import UMC_7th.Closit.domain.battle.dto.BattleLikeDTO.BattleLikeResponseDTO;
import UMC_7th.Closit.domain.battle.entity.Battle;
import UMC_7th.Closit.domain.battle.entity.BattleLike;
import UMC_7th.Closit.domain.user.entity.User;
import org.springframework.data.domain.Slice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class BattleLikeConverter {

    public static BattleLike toBattleLike (User user, Battle battle) { // 배틀 좋아요 생성
        return BattleLike.builder()
                .user(user)
                .battle(battle)
                .build();
    }

    public static BattleLikeResponseDTO.CreateBattleLikeResultDTO createBattleLikeResultDTO(BattleLike battleLike) {
        return BattleLikeResponseDTO.CreateBattleLikeResultDTO.builder()
                .battleLikeId(battleLike.getId())
                .clositId(battleLike.getUser().getClositId())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static BattleLikeResponseDTO.BattleLikePreviewDTO battleLikePreviewDTO (BattleLike battleLike) { // 배틀 좋아요 조회
        return BattleLikeResponseDTO.BattleLikePreviewDTO.builder()
                .battleLikeId(battleLike.getId())
                .clositId(battleLike.getUser().getClositId())
                .createdAt(battleLike.getCreatedAt())
                .build();
    }

    public static BattleLikeResponseDTO.BattleLikePreviewListDTO battleLikePreviewListDTO (Slice<BattleLike> battleLikeList) {
        List<BattleLikeResponseDTO.BattleLikePreviewDTO> battleLikePreviewDTOList = battleLikeList.stream()
                .map(BattleLikeConverter::battleLikePreviewDTO).collect(Collectors.toList());

        return BattleLikeResponseDTO.BattleLikePreviewListDTO.builder()
                .battleLikePreviewDTOList(battleLikePreviewDTOList)
                .listSize(battleLikePreviewDTOList.size())
                .isFirst(battleLikeList.isFirst())
                .isLast(battleLikeList.isLast())
                .hasNext(battleLikeList.hasNext())
                .build();
    }
}