package UMC_7th.Closit.global.s3;

import UMC_7th.Closit.domain.user.entity.User;
import UMC_7th.Closit.global.apiPayload.ApiResponse;
import UMC_7th.Closit.security.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "[Presigned URL 생성]")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class S3Controller {

    private final S3Service s3Service;
    private final SecurityUtil securityUtil;

    @Operation(summary = "Presigned URL 생성")
    @GetMapping("/presigned-url/{file_name}")
    public ApiResponse<String> getPresignedUrl(@PathVariable("file_name") String fileName) {
        User user = securityUtil.getCurrentUser();

        return ApiResponse.onSuccess(s3Service.getPresignedUrl("images", fileName));
    }
}
