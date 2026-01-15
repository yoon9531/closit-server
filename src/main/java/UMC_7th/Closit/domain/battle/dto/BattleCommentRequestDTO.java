package UMC_7th.Closit.domain.battle.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

public class BattleCommentRequestDTO {

    @Getter
    public static class CreateBattleCommentDTO { // 배틀 댓글 생성
        @NotBlank
        private String content;
        private Long parentCommentId;
    }
}
