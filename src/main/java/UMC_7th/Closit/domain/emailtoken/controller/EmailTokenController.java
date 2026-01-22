package UMC_7th.Closit.domain.emailtoken.controller;

import UMC_7th.Closit.domain.emailtoken.service.EmailTokenService;
import UMC_7th.Closit.global.apiPayload.ApiResponse;
import UMC_7th.Closit.global.util.HtmlTemplateUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "[이메일 인증]", description = "이메일 인증 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/email-tokens")
public class EmailTokenController {

    private final EmailTokenService emailTokenService;

    @Operation(summary = "이메일 인증 메일 전송",
            description = """
            ## 이메일 인증 메일 전송
            입력한 이메일 주소로 인증 링크가 포함된 메일을 발송합니다.

            ### Request Parameters
            - email: 인증 메일을 받을 이메일 주소
            """)
    @PostMapping
    public ApiResponse<String> requestEmailVerification(@RequestParam String email) {
        emailTokenService.createEmailToken(email);
        return ApiResponse.onSuccess("인증 이메일을 발송했습니다.");
    }

    @Operation(summary = "이메일 인증 확인",
            description = """
            ## 이메일 인증 확인
            이메일로 발송된 인증 링크를 통해 인증을 완료합니다.

            ### Request Parameters
            - token: 이메일 링크에 포함된 인증 토큰
            """)
    @GetMapping("/verify")
    public ResponseEntity<String> verifyEmail(@RequestParam String token) {
        try {
            emailTokenService.verifyEmailToken(token);
            String html = HtmlTemplateUtil.loadHtml("email-verification-success.html");
            return ResponseEntity.ok()
                    .contentType(MediaType.valueOf("text/html;charset=UTF-8"))
                    .body(html);
        } catch (Exception e) {
            String html = HtmlTemplateUtil.loadHtml("email-verification-failed.html");
            return ResponseEntity.ok()
                    .contentType(MediaType.valueOf("text/html;charset=UTF-8"))
                    .body(html);
        }
    }

    @Operation(summary = "이메일 인증 여부 확인",
            description = """
            ## 이메일 인증 여부 확인
            해당 이메일 주소가 인증되었는지 여부를 확인합니다.

            ### Request Parameters
            - email: 인증 여부를 확인할 이메일 주소
            """)
    @GetMapping("/verified")
    public ApiResponse<Boolean> isEmailVerified(@RequestParam String email) {
        return ApiResponse.onSuccess(emailTokenService.isEmailVerified(email));
    }
}
