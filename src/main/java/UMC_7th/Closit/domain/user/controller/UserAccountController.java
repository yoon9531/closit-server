package UMC_7th.Closit.domain.user.controller;

import UMC_7th.Closit.domain.user.dto.UserRequestDTO;
import UMC_7th.Closit.domain.user.service.UserCommandService;
import UMC_7th.Closit.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@Slf4j
@Validated
public class UserAccountController {

    private final UserCommandService userCommandService;

    @Operation(summary = "사용자 탈퇴 취소", description = "탈퇴 유예 기간 내 탈퇴를 취소(계정 복구)합니다.")
    @PostMapping("/withdrawal/cancel")
    public ApiResponse<String> cancelWithdrawal() {
        userCommandService.cancelWithdrawal();
        return ApiResponse.onSuccess("탈퇴가 취소되어 계정이 복구되었습니다.");
    }

    @Operation(summary = "사용자 탈퇴 요청", description = "사용자가 탈퇴를 요청하면 7일 유예 상태로 변경됩니다.")
    @PostMapping("/withdrawal")
    public ApiResponse<String> requestWithdrawal() {
        userCommandService.deleteUser();
        return ApiResponse.onSuccess("탈퇴 요청이 정상적으로 처리되었습니다. 7일 이내에 취소하지 않으면 계정이 완전히 삭제됩니다.");
    }

    @Operation(summary = "사용자 비활성화", description = "특정 사용자를 비활성화합니다.")
    @PatchMapping("/deactivate")
    public ApiResponse<String> deactivateUser(@RequestBody @Valid UserRequestDTO.DeactivateUserDTO deactivateUserDTO) {
        userCommandService.deactivateUser(deactivateUserDTO);
        return ApiResponse.onSuccess("사용자가 비활성화되었습니다.");
    }
}
