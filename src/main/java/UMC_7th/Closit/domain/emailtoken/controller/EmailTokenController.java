package UMC_7th.Closit.domain.emailtoken.controller;

import UMC_7th.Closit.domain.emailtoken.service.EmailTokenService;
import UMC_7th.Closit.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

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
    public ResponseEntity<String> verifyEmail(@RequestParam String token) {
        try {
            emailTokenService.verifyEmailToken(token);
            String html = readHtmlFromStatic("email-verification-success.html");
            return ResponseEntity.ok()
                    .contentType(MediaType.valueOf("text/html;charset=UTF-8"))
                    .body(html);
        } catch (Exception e) {
            String html = readHtmlFromStatic("email-verification-failed.html");
            return ResponseEntity.ok()
                    .contentType(MediaType.valueOf("text/html;charset=UTF-8"))
                    .body(html);
        }
    }

    @Operation(summary = "이메일 인증 여부 확인")
    @GetMapping("/verified")
    public ApiResponse<Boolean> isEmailVerified(@RequestParam String email) {
        return ApiResponse.onSuccess(emailTokenService.isEmailVerified(email));
    }

    private String readHtmlFromStatic(String filename) {
        try {
            ClassPathResource resource = new ClassPathResource("static/" + filename);
            Path path = resource.getFile().toPath();
            return Files.readString(path);
        } catch (IOException e) {
            return "<h1>페이지 로드 실패</h1>";
        }
    }
}
