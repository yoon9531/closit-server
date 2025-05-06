package UMC_7th.Closit.domain.notification.firebase.controller;

import UMC_7th.Closit.domain.notification.firebase.dto.FcmTokenRequest;
import UMC_7th.Closit.domain.notification.firebase.service.FcmTokenService;
import UMC_7th.Closit.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1")
public class FirebaseController {

    private final FcmTokenService fcmTokenService;

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/fcm-token")
    @Operation(summary = "FCM Token 저장",
            description = """
            ## 프론트에서 요청한 토큰 저장
            ### RequestBody
            token [저장할 FCM Token]
            """)
    public ApiResponse<String> getToken(@RequestBody @Valid FcmTokenRequest request) {
        fcmTokenService.getFcmToken(request);
        return ApiResponse.onSuccess("FCM Token registered successfully.");
    }
}
