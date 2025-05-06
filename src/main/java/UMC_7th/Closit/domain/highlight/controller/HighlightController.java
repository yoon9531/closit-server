package UMC_7th.Closit.domain.highlight.controller;

import UMC_7th.Closit.domain.highlight.converter.HighlightConverter;
import UMC_7th.Closit.domain.highlight.dto.HighlightResponseDTO;
import UMC_7th.Closit.domain.highlight.dto.HighlightRequestDTO;
import UMC_7th.Closit.domain.highlight.entity.Highlight;
import UMC_7th.Closit.domain.highlight.service.HighlightCommandService;
import UMC_7th.Closit.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/highlights")
public class HighlightController {

    private final HighlightCommandService highlightCommandService;

    @Operation(summary = "하이라이트 생성", description = "새로운 하이라이트를 생성합니다.")
    @PostMapping
    public ApiResponse<HighlightResponseDTO.CreateHighlightResultDTO> createHighlight(@RequestBody @Valid HighlightRequestDTO.CreateHighlightDTO request) {
        Highlight highlight = highlightCommandService.createHighlight(request);
        return ApiResponse.onSuccess(HighlightConverter.toCreateHighlightResultDTO(highlight));
    }

    @Operation(summary = "하이라이트 삭제", description = "게시글 ID를 통해 특정 하이라이트를 삭제합니다.")
    @DeleteMapping("/{post_id}")
    public ApiResponse<String> deleteHighlight(@PathVariable Long post_id) {
        highlightCommandService.deleteHighlight(post_id);
        return ApiResponse.onSuccess("Deleted Highlight with post ID: " + post_id);
    }
}
