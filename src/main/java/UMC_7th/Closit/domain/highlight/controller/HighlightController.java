package UMC_7th.Closit.domain.highlight.controller;

import UMC_7th.Closit.domain.highlight.converter.HighlightConverter;
import UMC_7th.Closit.domain.highlight.dto.HighlightResponseDTO;
import UMC_7th.Closit.domain.highlight.dto.HighlightRequestDTO;
import UMC_7th.Closit.domain.highlight.entity.Highlight;
import UMC_7th.Closit.domain.highlight.service.HighlightCommandService;
import UMC_7th.Closit.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "[하이라이트]", description = "하이라이트 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/highlights")
public class HighlightController {

    private final HighlightCommandService highlightCommandService;

    @Operation(summary = "하이라이트 생성",
            description = """
            ## 하이라이트 생성
            특정 게시글을 하이라이트로 등록합니다.

            ### Request Body
            - post_id: 하이라이트로 지정할 게시글의 ID
            """)
    @PostMapping
    public ApiResponse<HighlightResponseDTO.CreateHighlightResultDTO> createHighlight(@RequestBody @Valid HighlightRequestDTO.CreateHighlightDTO request) {
        Highlight highlight = highlightCommandService.createHighlight(request);
        return ApiResponse.onSuccess(HighlightConverter.toCreateHighlightResultDTO(highlight));
    }

    @Operation(summary = "하이라이트 삭제",
            description = """
            ## 하이라이트 삭제
            게시글 ID를 통해 등록된 하이라이트를 삭제합니다.

            ### PathVariable
            - post_id: 하이라이트에서 제거할 게시글의 ID
            """)
    @DeleteMapping("/{post_id}")
    public ApiResponse<String> deleteHighlight(@PathVariable Long post_id) {
        highlightCommandService.deleteHighlight(post_id);
        return ApiResponse.onSuccess("Deleted Highlight with post ID: " + post_id);
    }
}
