package UMC_7th.Closit.domain.todaycloset.controller;

import UMC_7th.Closit.domain.todaycloset.converter.TodayClosetConverter;
import UMC_7th.Closit.domain.todaycloset.dto.TodayClosetRequestDTO;
import UMC_7th.Closit.domain.todaycloset.dto.TodayClosetResponseDTO;
import UMC_7th.Closit.domain.todaycloset.entity.TodayCloset;
import UMC_7th.Closit.domain.todaycloset.service.TodayClosetQueryService;
import UMC_7th.Closit.domain.todaycloset.service.TodayClosetService;
import UMC_7th.Closit.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/auth/communities/todayclosets")
public class TodayClosetController {
    private final TodayClosetService todayClosetService;
    private final TodayClosetQueryService todayClosetQueryService;

    @Operation(summary = "오늘의 옷장 게시글 업로드")
    @PostMapping
    public ApiResponse<TodayClosetResponseDTO.CreateResponseDTO> createTodayCloset(
            @RequestBody TodayClosetRequestDTO.CreateRequestDTO request) {
        return ApiResponse.onSuccess(todayClosetService.createTodayCloset(request));
    }

    @Operation(summary = "오늘의 옷장 게시글 조회")
    @GetMapping
    public ApiResponse<TodayClosetResponseDTO.TodayClosetListDTO> getTodayClosetList(
            @RequestParam(name = "page") Integer page) {

        Slice<TodayCloset> todayClosetList = todayClosetQueryService.getTodayClosetList(page);
        return ApiResponse.onSuccess(TodayClosetConverter.toListDTO(todayClosetList));
    }

    @Operation(summary = "오늘의 옷장 게시글 삭제")
    @DeleteMapping("/{today_closet_id}")
    public ApiResponse<String> deleteTodayCloset(@PathVariable("today_closet_id") Long today_closet_id) {
        todayClosetService.deleteTodayCloset(today_closet_id);
        return ApiResponse.onSuccess("오늘의 옷장 삭제 성공");
    }

    @Operation(summary = "오늘의 옷장 - 사용자가 선택한 게시글 등록")
    @PostMapping("/2")
    public ApiResponse<TodayClosetResponseDTO.CreateResponseDTO> createTodayClosetBySelectedPost(
            @RequestBody TodayClosetRequestDTO.CreateRequestDTO request) {
        return ApiResponse.onSuccess(todayClosetService.createTodayClosetBySelectedPost(request));
    }
}


