package UMC_7th.Closit.domain.user.dto;

import UMC_7th.Closit.global.validation.annotation.ExistUserClositId;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

public class UserRequestDTO {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class CreateUserDTO {
        @NotBlank(message = "이름은 필수 입력 값입니다.")
        @Size(min = 2, max = 20, message = "이름은 2~20자 사이여야 합니다.")
        private String name;

        @NotBlank(message = "이메일은 필수 입력 값입니다.")
        @Email(message = "올바른 이메일 형식이어야 합니다.")
        private String email;

        @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
        @Size(min = 8, message = "비밀번호는 최소 8자리 이상이어야 합니다.")
        private String password;

        @NotBlank(message = "clositId는 필수 입력 값입니다.")
        @Size(min = 2, max = 20, message = "clositId는 2~20자 사이여야 합니다.")
        private String clositId;

        @PastOrPresent(message = "생년월일은 과거나 현재 날짜여야 합니다.")
        private LocalDate birth;
    }

    @Getter
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL) // null 값은 JSON 변환 시 제외
    public static class UpdateUserDTO {

        @Size(min = 2, max = 20, message = "이름은 2~20자 사이여야 합니다.")
        private String name;

//        @Size(min = 2, max = 20, message = "clositId는 2~20자 사이여야 합니다.")
//        private String clositId;

        @NotBlank(message = "현재 비밀번호는 필수 입력 값입니다.")
        private String currentPassword;

        @Size(min = 8, message = "비밀번호는 최소 8자리 이상이어야 합니다.")
        private String password;

        @PastOrPresent(message = "생년월일은 과거나 현재 날짜여야 합니다.")
        private LocalDate birth;
    }

    @Getter
    @AllArgsConstructor
    public static class BlockUserDTO {
        @NotBlank(message = "차단자의 clositId는 필수 입력 값입니다.")
        @ExistUserClositId
        private String blockerClositId;

        @NotBlank(message = "차단할 clositId는 필수 입력 값입니다.")
        @ExistUserClositId
        private String blockedClositId;
    }
}
