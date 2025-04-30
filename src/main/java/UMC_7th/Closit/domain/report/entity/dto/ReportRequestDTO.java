package UMC_7th.Closit.domain.report.entity.dto;

import UMC_7th.Closit.domain.report.entity.Description;
import UMC_7th.Closit.domain.report.entity.Type;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Builder
@Getter
@AllArgsConstructor
public class ReportRequestDTO {
    @NotNull(message = "receiverId는 필수값입니다.")
    private String receiverClositId;

    @NotNull(message = "type은 필수값입니다.")
    private Type type;

    @NotNull(message = "description은 필수값입니다.")
    private Description description;

    @Size(max = 500, message = "otherReason은 최대 500자까지 입력 가능합니다.")
    private String otherReason; // 기타 사유
}
