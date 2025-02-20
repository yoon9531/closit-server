package UMC_7th.Closit.domain.user.dto;

import UMC_7th.Closit.domain.highlight.dto.HighlightResponseDTO;
import UMC_7th.Closit.domain.user.converter.UserConverter;
import UMC_7th.Closit.domain.user.entity.Role;
import UMC_7th.Closit.domain.user.entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Slice;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class UserResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserDTO {
        private String clositId;
        private String name;
        private String email;
        @JsonFormat(pattern = "yyyy/MM/dd")
        private LocalDate birth;
        private String profileImage;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserHighlightSliceDTO {
        private List<HighlightResponseDTO.HighlightDTO> highlights;
        private boolean hasNext;
        private int pageNumber;
        private int size;

        public static UserHighlightSliceDTO from(Slice<HighlightResponseDTO.HighlightDTO> slice) {
            return UserHighlightSliceDTO.builder()
                    .highlights(slice.getContent())
                    .hasNext(slice.hasNext())
                    .pageNumber(slice.getNumber())
                    .size(slice.getSize())
                    .build();
        }
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserFollowerSliceDTO {
        private List<UserDTO> followers;
        private boolean hasNext;
        private int pageNumber;
        private int size;

        public static UserFollowerSliceDTO from(Slice<User> slice) {
            return UserFollowerSliceDTO.builder()
                    .followers(slice.map(UserConverter::toUserDTO).getContent())
                    .hasNext(slice.hasNext())
                    .pageNumber(slice.getNumber())
                    .size(slice.getSize())
                    .build();
        }
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserFollowingSliceDTO {
        private List<UserDTO> followings;
        private boolean hasNext;
        private int pageNumber;
        private int size;

        public static UserFollowingSliceDTO from(Slice<User> slice) {
            return UserFollowingSliceDTO.builder()
                    .followings(slice.map(UserConverter::toUserDTO).getContent())
                    .hasNext(slice.hasNext())
                    .pageNumber(slice.getNumber())
                    .size(slice.getSize())
                    .build();
        }
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateUserInfoDTO {
//        private Long id;                // User ID
        private Role role;              // USer role
        private String clositId;        // 사용자 닉네임
        private String name;            // 사용자 이름
        private String email;           // 이메일
        @JsonFormat(pattern = "yyyy/MM/dd")
        private LocalDate birth;        // 생년월일
        private String profileImage;    // 프로필 이미지
        private long followers;
        private long following;
        @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
        private LocalDateTime createdAt;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class UserInfoDTO {
//        private Long id;
        private Role role;
        private String clositId;
        private String name;
        private String email;
        @JsonFormat(pattern = "yyyy/MM/dd")
        private LocalDate birth;
        private String profileImage;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserRecentPostDTO { // 특정 사용자 최근 게시글 조회
        private String clositId;
        private String userName;
        private Long postId;
        private String thumbnail;
        @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
        private LocalDateTime createdAt;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserRecentPostListDTO {
        private List<UserRecentPostDTO> userRecentPostDTOList;
        private Integer listSize;
        private boolean isFirst;
        private boolean isLast;
        private boolean hasNext;
    }
}
