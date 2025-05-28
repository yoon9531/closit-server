package UMC_7th.Closit.domain.follow.controller;

import UMC_7th.Closit.domain.follow.converter.FollowConverter;
import UMC_7th.Closit.domain.follow.dto.FollowRequestDTO;
import UMC_7th.Closit.domain.follow.dto.FollowResponseDTO;
import UMC_7th.Closit.domain.follow.entity.Follow;
import UMC_7th.Closit.domain.follow.service.FollowCommandService;
import UMC_7th.Closit.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "[팔로우]", description = "팔로우 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/follows")
public class FollowController {

    private final FollowCommandService followCommandService;

    @Operation(summary = "팔로우 생성",
            description = """
            ## 팔로우 생성
            특정 사용자를 팔로우합니다.

            ### Request Body
            - receiver_closit_id: 팔로우할 대상 유저의 Closit ID
            """)
    @PostMapping
    public ApiResponse<FollowResponseDTO.CreateFollowResultDTO> createFollow(@RequestBody @Valid FollowRequestDTO.CreateFollowDTO request) {
        Follow follow = followCommandService.createFollow(request);
        return ApiResponse.onSuccess(FollowConverter.toCreateFollowResultDTO(follow));
    }

    @Operation(summary = "팔로우 여부 조회",
            description = """
            ## 팔로우 여부 조회
            특정 유저를 현재 로그인한 유저가 팔로우하고 있는지 확인합니다.

            ### PathVariable
            - receiver_closit_id: 팔로우 대상 유저의 Closit ID
            """)
    @GetMapping("/{receiver_closit_id}")
    public ApiResponse<Boolean> checkFollow(@PathVariable String receiver_closit_id) {
        return ApiResponse.onSuccess(followCommandService.isFollowing(receiver_closit_id));
    }

    @Operation(summary = "팔로우 삭제",
            description = """
        ## 팔로우 삭제
        특정 유저에 대한 팔로우를 취소합니다.

        ### PathVariable
        - receiver_closit_id: 언팔로우할 대상 유저의 Closit ID
        """)
    @DeleteMapping("/{receiver_closit_id}")
    public ApiResponse<String> deleteFollow(@PathVariable String receiver_closit_id) {
        followCommandService.deleteFollow(receiver_closit_id);
        return ApiResponse.onSuccess("Deleted Follow with receiver closit ID: " + receiver_closit_id);
    }
}
