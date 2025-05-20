package UMC_7th.Closit.domain.post.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemTagDTO {

    private Double x; // X 좌표
    private Double y; // Y 좌표
    @Size(max = 20, message = "해시태그는 20자 이내여야 합니다.")
    private String content; // 태그 내용
}
