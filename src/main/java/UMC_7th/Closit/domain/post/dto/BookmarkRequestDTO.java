package UMC_7th.Closit.domain.post.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class BookmarkRequestDTO {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateBookmarkDTO { // 북마크 생성
        @NotNull
        private Long postId;
    }
}
