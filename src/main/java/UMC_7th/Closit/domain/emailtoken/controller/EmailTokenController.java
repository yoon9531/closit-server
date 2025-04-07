package UMC_7th.Closit.domain.emailtoken.controller;

import UMC_7th.Closit.domain.emailtoken.service.EmailTokenService;
import UMC_7th.Closit.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/email-tokens")
public class EmailTokenController {

    private final EmailTokenService emailTokenService;

    @Operation(summary = "이메일 인증 메일 전송", description = "입력한 이메일로 인증 링크를 발송합니다.")
    @PostMapping
    public ApiResponse<String> requestEmailVerification(@RequestParam String email) {
        emailTokenService.createEmailToken(email);
        return ApiResponse.onSuccess("인증 이메일을 발송했습니다.");
    }

    @Operation(summary = "이메일 인증 확인", description = "이메일로 발송된 링크를 통해 인증을 완료합니다.")
    @GetMapping("/verify")
    public ApiResponse<String> verifyEmail(@RequestParam String token) {
        emailTokenService.verifyEmailToken(token);
        return ApiResponse.onSuccess("이메일 인증이 완료되었습니다.");
    }
}
