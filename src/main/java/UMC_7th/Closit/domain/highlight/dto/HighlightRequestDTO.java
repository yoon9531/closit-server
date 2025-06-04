package UMC_7th.Closit.domain.highlight.dto;

import UMC_7th.Closit.global.validation.annotation.ExistPost;
import UMC_7th.Closit.global.validation.annotation.ExistUser;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

public class HighlightRequestDTO {

    @Getter
    public static class CreateHighlightDTO {

        @NotNull(message = "게시글 id는 필수 입력 값입니다.")
        @ExistPost
        private Long postId;
    }
}
