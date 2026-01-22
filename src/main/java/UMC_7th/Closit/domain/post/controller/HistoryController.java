package UMC_7th.Closit.domain.post.controller;

import UMC_7th.Closit.domain.post.converter.HistoryConverter;
import UMC_7th.Closit.domain.post.dto.HistoryResponseDTO;
import UMC_7th.Closit.domain.post.entity.Post;
import UMC_7th.Closit.domain.post.service.HistoryQueryService;
import UMC_7th.Closit.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/history")
public class HistoryController {

    private final HistoryQueryService historyQueryService;

    @GetMapping
    @Operation(summary = "사용자의 히스토리 게시글 썸네일 조회",
            description = """
                    ## 사용자 날짜 별 히스토리 게시글 썸네일 조회
                    ### Parameters
                    page [조회할 페이지 번호] - 0부터 시작, 30개씩 보여줌
                    """)
    public ApiResponse<HistoryResponseDTO.DataHistoryThumbnailListDTO> dateThumbnailList (@RequestParam(name = "page") Integer page) { // 히스토리 썸네일 조회

        Slice<Post> dateThumbnailList = historyQueryService.getHistoryThumbnailList(page);

        return ApiResponse.onSuccess(HistoryConverter.dataHistoryThumbnailListDTO(dateThumbnailList));
    }

    @GetMapping("/detail")
    @Operation(summary = "사용자의 히스토리 상세 조회",
            description = """
                    ## 사용자 날짜 별 히스토리 상세 조회
                    ### Parameters
                    localDate [조회할 히스토리 날짜] - e.g. 2025-02-14
                    """)
    public ApiResponse<HistoryResponseDTO.DateHistoryPreviewListDTO> datePreviewList (@RequestParam(name = "localDate") LocalDate localDate) { // 히스토리 게시글 상세 조회

        List<Post> datePreviewList = historyQueryService.getHistoryPreviewList(localDate);

        return ApiResponse.onSuccess(HistoryConverter.dateHistoryPreviewListDTO(localDate, datePreviewList));
    }

    @GetMapping("/pointcolor")
    @Operation(summary = "사용자의 히스토리 포인트 컬러 썸네일 조회",
            description = """
                    ## 사용자 날짜 별 히스토리 포인트 컬러 썸네일 조회
                    ### Parameters
                    page [조회할 페이지 번호] - 0부터 시작, 30개씩 보여줌
                    """)
    public ApiResponse<HistoryResponseDTO.ColorHistoryThumbnailListDTO> colorThumbnailList (@RequestParam(name = "page") Integer page) { // 히스토리 포인트 색상 썸네일 조회

        Slice<Post> colorThumbnailList = historyQueryService.getHistoryColorThumbnailList(page);

        return ApiResponse.onSuccess(HistoryConverter.colorHistoryThumbnailListDTO(colorThumbnailList));
    }
}