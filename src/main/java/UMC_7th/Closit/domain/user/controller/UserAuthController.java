package UMC_7th.Closit.domain.user.controller;

import UMC_7th.Closit.domain.user.dto.*;
import UMC_7th.Closit.domain.user.entity.Role;
import UMC_7th.Closit.domain.user.service.UserAuthService;
import UMC_7th.Closit.domain.user.service.UserCommandService;
import UMC_7th.Closit.global.apiPayload.ApiResponse;
import UMC_7th.Closit.global.common.SocialLoginType;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class UserAuthController {

    private final UserCommandService userCommandService;
    private final UserAuthService userAuthService;

    @PostMapping("/register")
    public ApiResponse<RegisterResponseDTO> register (@RequestBody @Valid UserRequestDTO.CreateUserDTO userRequestDto){
        RegisterResponseDTO responseDto = userCommandService.registerUser(userRequestDto);

        return ApiResponse.onSuccess(responseDto);
    }

    @PostMapping("/login")
    public ApiResponse<JwtResponse> login (@RequestBody @Valid LoginRequestDTO loginRequestDto) {
        JwtResponse jwtResponse = userAuthService.login(loginRequestDto);

        return ApiResponse.onSuccess(jwtResponse);
    }

    @Operation(summary = "소셜 로그인", description = "소셜 로그인 API")
    @PostMapping("/{socialLoginType}")
    public ApiResponse<JwtResponse> socialLogin(@PathVariable SocialLoginType socialLoginType, @RequestBody OAuthLoginRequestDTO socialLoginRequestDTO) {
        JwtResponse jwtResponse = userAuthService.socialLogin(socialLoginType, socialLoginRequestDTO);

        return ApiResponse.onSuccess(jwtResponse);
    }

    @PostMapping("/refresh")
    public ApiResponse<JwtResponse> refresh(@RequestBody RefreshRequestDTO refreshRequestDTO) {
        String refreshToken = refreshRequestDTO.getRefreshToken();

        JwtResponse jwtResponse = userAuthService.refresh(refreshToken);

        return ApiResponse.onSuccess(jwtResponse);
    }

    @PatchMapping("/{user_id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<UserResponseDTO.UserInfoDTO> changeRole(@PathVariable Long user_id, @RequestParam Role newRole) {
        UserResponseDTO.UserInfoDTO userInfoDTO = userAuthService.updateUserRole(user_id, newRole);

        return ApiResponse.onSuccess(userInfoDTO);
    }
}
