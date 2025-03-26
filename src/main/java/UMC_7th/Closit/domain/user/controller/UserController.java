package UMC_7th.Closit.domain.user.controller;

import UMC_7th.Closit.domain.highlight.entity.Highlight;
import UMC_7th.Closit.domain.post.entity.Post;
import UMC_7th.Closit.domain.user.converter.UserConverter;
import UMC_7th.Closit.domain.user.dto.UserRequestDTO;
import UMC_7th.Closit.domain.user.dto.UserResponseDTO;
import UMC_7th.Closit.domain.user.entity.User;
import UMC_7th.Closit.domain.user.service.UserCommandService;
import UMC_7th.Closit.domain.user.service.UserQueryService;
import UMC_7th.Closit.global.apiPayload.ApiResponse;
import UMC_7th.Closit.global.validation.annotation.CheckBlocked;
import UMC_7th.Closit.global.s3.S3Service;
import UMC_7th.Closit.security.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/users")
@Slf4j
public class UserController {

    private final SecurityUtil securityUtil;
    private final S3Service s3Service;
    private final UserCommandService userCommandService;
    private final UserQueryService userQueryService;

    @Value("${cloud.aws.s3.path.profileImage}")
    private String profileImagePath;

    @Operation(summary = "사용자 삭제", description = "특정 사용자를 삭제합니다.")
    @DeleteMapping()
    public ApiResponse<String> deleteUser() {
        userCommandService.deleteUser();
        return ApiResponse.onSuccess("Deletion successful");
    }

    @Operation(summary = "사용자 정보 수정", description = "사용자 정보를 수정합니다.")
    @PatchMapping()
    public ApiResponse<UserResponseDTO.UserInfoDTO> updateUserInfo(@Valid @RequestBody UserRequestDTO.UpdateUserDTO updateUserDTO) {

        User userInfo = userCommandService.updateUserInfo(updateUserDTO);

        return ApiResponse.onSuccess(UserConverter.toUserInfoDTO(userInfo));
    }

    @PostMapping("/profile-image/presigned-url")
    @Operation(summary = "사용자 프로필 이미지 Presigned Url 발급",
            description = """
                    ## 사용자 프로필 이미지 등록을 위한 Presigned Url 발급
                    ### RequestBody
                    imageUrl [사용자 프로필 이미지 파일명]
                    ### 등록 과정
                    1. 등록할 프로필 이미지 파일명 작성
                    2. 발급 받은 Presigned Url + 실제 파일 (Body -> binary)과 함께 PUT 요청 (Postman에서 진행)
                    3. 200 OK -> S3 업로드 성공
                    4. 성공 후, 사용자 프로필 이미지 등록 API에 Presigned Url의 쿼리 파라미터를 제외하고 업로드 요청
                    """)
    public ApiResponse<UserResponseDTO.CreatePresignedUrlDTO> getPresignedUrl(@RequestBody @Valid UserRequestDTO.UpdateProfileImageDTO request) {
        User user = securityUtil.getCurrentUser();

        String imageUrl = s3Service.getPresignedUrl(profileImagePath, request.getImageUrl());

        return ApiResponse.onSuccess(UserConverter.createPresignedUrlDTO(imageUrl));
    }

    @Operation(summary = "사용자 프로필 이미지 등록", description = "특정 사용자의 프로필 이미지를 등록합니다.")
    @PatchMapping(value = "/profile-image")
    public ApiResponse<UserResponseDTO.UserInfoDTO> registerProfileImage(@RequestBody @Valid UserRequestDTO.UpdateProfileImageDTO request) {
        User userInfo = userCommandService.registerProfileImage(request.getImageUrl());
        return ApiResponse.onSuccess(UserConverter.toUserInfoDTO(userInfo));
    }

    @Operation(summary = "사용자 정보 조회", description = "특정 사용자의 정보를 조회합니다.")
    @GetMapping("/{closit_id}")
    @CheckBlocked(targetIdParam = "closit_id")
    public ApiResponse<UserResponseDTO.UpdateUserInfoDTO> getUserInfo (@PathVariable String closit_id) {
        return ApiResponse.onSuccess(userQueryService.getUserInfo(closit_id));
    }

    @Operation(summary = "사용자 차단", description = "특정 사용자를 차단합니다.")
    @PostMapping("/block")
    public ApiResponse<String> blockUser(@RequestBody UserRequestDTO.BlockUserDTO blockUserDTO) {
        userCommandService.blockUser(blockUserDTO);
        return ApiResponse.onSuccess("User blocked successfully");
    }

    @Operation(summary = "사용자 차단 해제", description = "특정 사용자의 차단을 해제합니다.")
    @DeleteMapping("/block")
    public ApiResponse<String> unblockUser(@RequestBody UserRequestDTO.BlockUserDTO blockUserDTO) {
        userCommandService.unblockUser(blockUserDTO);
        return ApiResponse.onSuccess("User unblocked successfully");
    }

    @Operation(summary = "사용자의 팔로워 목록 조회", description = "특정 사용자의 팔로워 목록을 조회합니다.")
    @CheckBlocked(targetIdParam = "closit_id")
    @GetMapping("/{closit_id}/followers")
    public ApiResponse<UserResponseDTO.UserFollowerSliceDTO> getUserFollowers (
            @PathVariable String closit_id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Slice<User> followerSlice = userQueryService.getFollowerList(closit_id, PageRequest.of(page, size));
        return ApiResponse.onSuccess(UserConverter.toUserFollowerSliceDTO(followerSlice));
    }

    @Operation(summary = "사용자의 팔로잉 목록 조회", description = "특정 사용자의 팔로잉 목록을 조회합니다.")
    @CheckBlocked(targetIdParam = "closit_id")
    @GetMapping("/{closit_id}/following")
    public ApiResponse<UserResponseDTO.UserFollowingSliceDTO> getUserFollowing(
            @PathVariable String closit_id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Slice<User> followingSlice = userQueryService.getFollowingList(closit_id, PageRequest.of(page, size));
        return ApiResponse.onSuccess(UserConverter.toUserFollowingSliceDTO(followingSlice));
    }

    @Operation(summary = "사용자의 하이라이트 목록 조회", description = "특정 사용자의 하이라이트 목록을 조회합니다.")
    @CheckBlocked(targetIdParam = "closit_id")
    @GetMapping("/{closit_id}/highlights")
    public ApiResponse<UserResponseDTO.UserHighlightSliceDTO> getUserHighlights(
            @PathVariable String closit_id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Slice<Highlight> highlightSlice = userQueryService.getHighlightList(closit_id, PageRequest.of(page, size));

        return ApiResponse.onSuccess(UserConverter.toUserHighlightSliceDTO(highlightSlice));
    }

    @Operation(summary = "사용자의 데일리 미션 완료 여부 조회", description = "특정 사용자가 데일리 미션을 완료했는지 여부를 조회합니다.")
    @GetMapping("/mission")
    public ApiResponse<Boolean> getUserMission() {
        return ApiResponse.onSuccess(userQueryService.isMissionDone());
    }

    @Operation(summary = "closit id 중복 여부 조회", description = "특정 closit id가 이미 있는 id인지 조회합니다.")
    @GetMapping("/isunique/{closit_id}")
    public ApiResponse<Boolean> isUniqueClositId(@PathVariable String closit_id) {
        return ApiResponse.onSuccess(userCommandService.isClositIdUnique(closit_id));
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
