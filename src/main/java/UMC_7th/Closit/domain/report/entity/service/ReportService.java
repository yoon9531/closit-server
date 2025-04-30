package UMC_7th.Closit.domain.report.entity.service;

import UMC_7th.Closit.domain.report.entity.Description;
import UMC_7th.Closit.domain.report.entity.Report;
import UMC_7th.Closit.domain.report.entity.dto.ReportRequestDTO;
import UMC_7th.Closit.domain.report.entity.repository.ReportRepository;
import UMC_7th.Closit.domain.user.entity.User;
import UMC_7th.Closit.domain.user.repository.UserRepository;
import UMC_7th.Closit.global.apiPayload.code.status.ErrorStatus;
import UMC_7th.Closit.global.apiPayload.exception.handler.UserHandler;
import UMC_7th.Closit.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ReportService {

    private final UserRepository userRepository;
    private final ReportRepository reportRepository;
    private final SecurityUtil securityUtil;

    public void reportUser(ReportRequestDTO reportRequestDTO) {

        User sender = securityUtil.getCurrentUser();
        User receiver = userRepository.findByClositId(reportRequestDTO.getReceiverClositId())
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        // otherReason : Description.OTHER 일 때만 사용
        String otherReason = reportRequestDTO.getDescription() == Description.OTHER
                ? reportRequestDTO.getOtherReason()
                : null;

        Report report = Report.builder()
                .sender(sender)
                .receiver(receiver)
                .type(reportRequestDTO.getType())
                .description(reportRequestDTO.getDescription())
                .otherReason(otherReason)
                .build();

        reportRepository.save(report);
    }
}
