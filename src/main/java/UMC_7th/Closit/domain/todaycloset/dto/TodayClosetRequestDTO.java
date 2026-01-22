package UMC_7th.Closit.domain.todaycloset.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class TodayClosetRequestDTO {
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class CreateRequestDTO{
        private Long postId;
    }
}
