package UMC_7th.Closit.domain.follow.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class FollowResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateFollowResultDTO {
        private Long followId;
        private Long senderId;
        private Long receiverId;
        @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
        private LocalDateTime createdAt;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FollowDTO {
        private Long followId;
        private Long senderId;
        private Long receiverId;
        @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
        private LocalDateTime createdAt;
        @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
        private LocalDateTime updatedAt;
    }
}
