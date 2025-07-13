package UMC_7th.Closit.domain.user.controller;

import UMC_7th.Closit.domain.user.converter.UserConverter;
import UMC_7th.Closit.domain.user.dto.UserRequestDTO;
import UMC_7th.Closit.domain.user.dto.UserResponseDTO;
import UMC_7th.Closit.domain.user.entity.User;
import UMC_7th.Closit.domain.user.service.UserCommandService;
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

// 사용자 간 상호작용 관리
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@Slf4j
@Validated
public class UserInteractionController {
    private final UserCommandService userCommandService;
    private final UserQueryService userQueryService;

    @Operation(summary = "사용자 차단", description = "특정 사용자를 차단합니다.")
    @PostMapping("/block")
    public ApiResponse<UserResponseDTO.UserBlockResponseDTO> blockUser(@RequestBody UserRequestDTO.BlockUserDTO blockUserDTO) {
        UserResponseDTO.UserBlockResponseDTO userBlockResponseDTO = userCommandService.blockUser(blockUserDTO);
        return ApiResponse.onSuccess(userBlockResponseDTO);
    }

    @Operation(summary = "사용자 차단 해제", description = "특정 사용자의 차단을 해제합니다.")
    @DeleteMapping("/block")
    public ApiResponse<String> unblockUser(@RequestBody UserRequestDTO.BlockUserDTO blockUserDTO) {
        userCommandService.unblockUser(blockUserDTO);
        return ApiResponse.onSuccess("User unblocked successfully");
    }

    @Operation(summary = "특정 사용자 차단 여부 조회", description = "특정 사용자의 차단 여부를 합니다.")
    @GetMapping("/block")
    public ApiResponse<UserResponseDTO.IsBlockedDTO> isBlockedBy(@RequestParam("closit_id") String closit_id) {
        return ApiResponse.onSuccess(userQueryService.isBlockedBy(closit_id));
    }

    @Operation(summary = "사용자 차단 목록 조회", description = "특정 사용자의 차단 목록을 조회합니다.")
    @GetMapping("/blocks")
    public ApiResponse<UserResponseDTO.UserBlockListDTO> getBlockedUserList(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {

        Slice<String> blockedUserSlice = userQueryService.getBlockedUserList(PageRequest.of(page, size));
        return ApiResponse.onSuccess(UserConverter.toUserBlockListDTO(blockedUserSlice));
    }

    @Operation(summary = "사용자의 팔로워 목록 조회",
            description = """
            ## 사용자의 팔로워 목록 조회
            특정 사용자의 팔로워 목록을 페이징하여 조회합니다.

            ### PathVariable
            - closit_id: 팔로워 목록을 조회할 사용자의 Closit ID

            ### Request Parameters
            - page (기본값: 0): 조회할 페이지 번호 (0부터 시작)
            - size (기본값: 10): 페이지당 항목 수
            """)
    @CheckBlocked(targetIdParam = "closit_id")
    @GetMapping("/{closit_id}/followers")
    public ApiResponse<UserResponseDTO.UserFollowerSliceDTO> getUserFollowers (
            @PathVariable("closit_id") String closit_id,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {

        Slice<User> followerSlice = userQueryService.getFollowerList(closit_id, PageRequest.of(page, size));
        return ApiResponse.onSuccess(UserConverter.toUserFollowerSliceDTO(followerSlice));
    }

    @Operation(summary = "사용자의 팔로잉 목록 조회",
            description = """
            ## 사용자의 팔로잉 목록 조회
            특정 사용자가 팔로우한 유저 목록(팔로잉 목록)을 페이징하여 조회합니다.

            ### PathVariable
            - closit_id: 팔로잉 목록을 조회할 사용자의 Closit ID

            ### Request Parameters
            - page (기본값: 0): 조회할 페이지 번호 (0부터 시작)
            - size (기본값: 10): 페이지당 항목 수
            """)
    @CheckBlocked(targetIdParam = "closit_id")
    @GetMapping("/{closit_id}/following")
    public ApiResponse<UserResponseDTO.UserFollowingSliceDTO> getUserFollowing(
            @PathVariable("closit_id") String closit_id,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {

        Slice<User> followingSlice = userQueryService.getFollowingList(closit_id, PageRequest.of(page, size));
        return ApiResponse.onSuccess(UserConverter.toUserFollowingSliceDTO(followingSlice));
    }
}
