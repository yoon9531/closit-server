package UMC_7th.Closit.domain.user.controller;

import UMC_7th.Closit.domain.highlight.entity.Highlight;
import UMC_7th.Closit.domain.post.entity.Post;
import UMC_7th.Closit.domain.user.converter.UserConverter;
import UMC_7th.Closit.domain.user.dto.UserResponseDTO;
import UMC_7th.Closit.domain.user.service.UserQueryService;
import UMC_7th.Closit.global.apiPayload.ApiResponse;
import UMC_7th.Closit.global.validation.annotation.CheckBlocked;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@Slf4j
@Validated
public class UserContentController {
    private final UserQueryService userQueryService;

    @Operation(summary = "사용자의 하이라이트 목록 조회",
            description = """
            ## 사용자의 하이라이트 목록 조회
            특정 사용자가 등록한 하이라이트 게시글 목록을 페이징하여 조회합니다.

            ### PathVariable
            - closit_id: 하이라이트 목록을 조회할 사용자의 Closit ID

            ### Request Parameters
            - page (기본값: 0): 조회할 페이지 번호 (0부터 시작)
            - size (기본값: 10): 페이지당 항목 수
            """)
    @CheckBlocked(targetIdParam = "closit_id")
    @GetMapping("/{closit_id}/highlights")
    public ApiResponse<UserResponseDTO.UserHighlightSliceDTO> getUserHighlights(
            @PathVariable("closit_id") String closit_id,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {

        Slice<Highlight> highlightSlice = userQueryService.getHighlightList(closit_id, PageRequest.of(page, size));

        return ApiResponse.onSuccess(UserConverter.toUserHighlightSliceDTO(highlightSlice));
    }

    @GetMapping("/{closit_id}/recent-post")
    @CheckBlocked(targetIdParam = "closit_id")
    @Operation(summary = "사용자의 최근 게시물 조회",
            description = """
            ## 특정 사용자의 최근 게시글 조회
            ### PathVariable
            closit_id [사용자의 closit ID]
            ### Parameters
            page [조회할 페이지 번호] - 0부터 시작, 10개씩 보여줌
            """)
    public ApiResponse<UserResponseDTO.UserRecentPostListDTO> getRecentPostList(@PathVariable("closit_id") String closit_id,
                                                                                @RequestParam(name = "page") Integer page) {

        Slice<Post> recentPostList = userQueryService.getRecentPostList(closit_id, page);

        return ApiResponse.onSuccess(UserConverter.userRecentPostListDTO(recentPostList));
    }
}
