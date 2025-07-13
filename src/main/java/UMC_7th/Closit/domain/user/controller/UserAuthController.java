package UMC_7th.Closit.domain.user.controller;

import UMC_7th.Closit.domain.emailtoken.service.EmailTokenService;
import UMC_7th.Closit.domain.user.dto.*;
import UMC_7th.Closit.domain.user.entity.Role;
import UMC_7th.Closit.domain.user.service.UserAuthService;
import UMC_7th.Closit.domain.user.service.UserCommandService;
import UMC_7th.Closit.global.apiPayload.ApiResponse;
import UMC_7th.Closit.global.apiPayload.code.status.ErrorStatus;
import UMC_7th.Closit.global.apiPayload.exception.handler.UserHandler;
import UMC_7th.Closit.global.common.SocialLoginType;
import UMC_7th.Closit.security.jwt.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
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
    private final EmailTokenService emailTokenService;
    private final JwtTokenProvider jwtTokenProvider;

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

    @PostMapping("/logout")
    public ApiResponse<String> logout(HttpServletRequest request) {
        String accessToken = jwtTokenProvider.resolveToken(request);
        if (accessToken == null) {
            throw new UserHandler(ErrorStatus._UNAUTHORIZED);
        }

        userAuthService.logout(accessToken);
        return ApiResponse.onSuccess("로그아웃이 완료되었습니다.");
    }

    @Operation(summary = "소셜 로그인", description = "소셜 로그인 API")
    @PostMapping("/oauth/{socialLoginType}")
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

    @PatchMapping("/{closit_id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<UserResponseDTO.UserInfoDTO> changeRole(@PathVariable("closit_id") String clositId, @RequestParam Role newRole) {
        UserResponseDTO.UserInfoDTO userInfoDTO = userAuthService.updateUserRole(clositId, newRole);

        return ApiResponse.onSuccess(userInfoDTO);
    }

    @Operation(summary = "Closit ID 찾기", description = "이메일 인증을 완료한 사용자에 한해 Closit ID를 조회합니다.")
    @GetMapping("/find-id")
    public ApiResponse<String> findClositId(@RequestParam String email) {
        String clositId = userAuthService.findClositIdByEmail(email);

        return ApiResponse.onSuccess(clositId);
    }

    @Operation(summary = "비밀번호 재설정", description = "이메일 인증을 완료한 사용자에 한해 비밀번호를 재설정합니다.")
    @PostMapping("/reset-password")
    public ApiResponse<String> resetPassword(@RequestBody @Valid PasswordResetRequestDTO request) {
        userAuthService.resetPassword(request.getEmail(), request.getNewPassword());

        return ApiResponse.onSuccess("비밀번호가 성공적으로 변경되었습니다.");
    }


}
