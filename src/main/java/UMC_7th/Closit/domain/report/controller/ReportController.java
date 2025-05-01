package UMC_7th.Closit.domain.report.controller;

import UMC_7th.Closit.domain.report.dto.ReportRequestDTO;
import UMC_7th.Closit.domain.report.service.ReportService;
import UMC_7th.Closit.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Slf4j
public class ReportController {

    private final ReportService reportService;

    @Operation(summary = "사용자 신고", description = "특정 사용자를 신고합니다.")
    @PostMapping("/report")
    public ApiResponse<String> reportUser(ReportRequestDTO reportRequestDTO) {
        reportService.reportUser(reportRequestDTO);
        return ApiResponse.onSuccess("신고가 완료되었습니다.");
    }

}
