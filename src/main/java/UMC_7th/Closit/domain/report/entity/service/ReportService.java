package UMC_7th.Closit.domain.report.entity.service;

import UMC_7th.Closit.domain.report.entity.Description;
import UMC_7th.Closit.domain.report.entity.Report;
import UMC_7th.Closit.domain.report.entity.dto.ReportRequestDTO;
import UMC_7th.Closit.domain.report.entity.repository.ReportRepository;
import UMC_7th.Closit.domain.user.entity.User;
import UMC_7th.Closit.domain.user.repository.UserRepository;
import UMC_7th.Closit.global.apiPayload.code.status.ErrorStatus;
import UMC_7th.Closit.global.apiPayload.exception.handler.UserHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportService {

    private final UserRepository userRepository;
    private final ReportRepository reportRepository;

    // 신고 처리 로직을 구현합니다.
    // 예를 들어, 신고 내용을 데이터베이스에 저장하거나, 신고된 사용자의 상태를 업데이트하는 등의 작업을 수행할 수 있습니다.
    // 이 부분은 비즈니스 로직에 따라 다르게 구현될 수 있습니다.

    // 예시로, 신고 내용을 데이터베이스에 저장하는 메서드를 작성할 수 있습니다.
    public void reportUser(ReportRequestDTO reportRequestDTO) {

        User sender = userRepository.findById(reportRequestDTO.getSenderId())
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));
        User receiver = userRepository.findById(reportRequestDTO.getReceiverId())
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
