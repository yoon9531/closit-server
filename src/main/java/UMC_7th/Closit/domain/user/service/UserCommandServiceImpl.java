package UMC_7th.Closit.domain.user.service;

import UMC_7th.Closit.domain.emailtoken.service.EmailTokenService;
import UMC_7th.Closit.domain.follow.entity.Follow;
import UMC_7th.Closit.domain.follow.repository.FollowRepository;
import UMC_7th.Closit.domain.user.converter.UserConverter;
import UMC_7th.Closit.domain.user.dto.RegisterResponseDTO;
import UMC_7th.Closit.domain.user.dto.UserRequestDTO;
import UMC_7th.Closit.domain.user.dto.UserResponseDTO;
import UMC_7th.Closit.domain.user.entity.User;
import UMC_7th.Closit.domain.user.entity.Block;
import UMC_7th.Closit.domain.user.repository.BlockRepository;
import UMC_7th.Closit.domain.user.repository.UserRepository;
import UMC_7th.Closit.global.apiPayload.code.status.ErrorStatus;
import UMC_7th.Closit.global.apiPayload.exception.handler.UserHandler;
import UMC_7th.Closit.global.s3.S3Service;
import UMC_7th.Closit.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static UMC_7th.Closit.domain.user.entity.Role.ADMIN;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserCommandServiceImpl implements UserCommandService {

    private final UserRepository userRepository;
    private final BlockRepository blockRepository;
    private final FollowRepository followRepository;
    private final EmailTokenService emailTokenService;
    private final PasswordEncoder passwordEncoder;
    private final SecurityUtil securityUtil;
    private final S3Service s3Service;

    @Value("${cloud.aws.s3.default-profile-image}")
    private String defaultProfileImage;

    @Override
    public RegisterResponseDTO registerUser(UserRequestDTO.CreateUserDTO userRequestDto) {

        // Email Already Exists
        if (userRepository.existsByEmail(userRequestDto.getEmail())) {
            throw new UserHandler(ErrorStatus.EMAIL_ALREADY_EXISTS);
        }
//
//        // 이메일 인증 여부 확인
//        if (!emailTokenService.isEmailVerified(userRequestDto.getEmail())) {
//            throw new UserHandler(ErrorStatus.EMAIL_NOT_VERIFIED);
//        }
//
//        // 인증 토큰 사용 처리
//        emailTokenService.markTokenAsUsed(userRequestDto.getEmail());

        // ClositId Already Exists
        if (userRepository.existsByClositId(userRequestDto.getClositId())) {
            throw new UserHandler(ErrorStatus.CLOSIT_ID_ALREADY_EXISTS);
        }

        // Password Encoding
        String encodedPassword = passwordEncoder.encode(userRequestDto.getPassword());

        // UserDto to User
        User user = User.builder()
                .name(userRequestDto.getName())
                .email(userRequestDto.getEmail())
                .clositId(userRequestDto.getClositId())
                .password(encodedPassword)
                .birth(userRequestDto.getBirth())
                .profileImage(defaultProfileImage)
                .role(UMC_7th.Closit.domain.user.entity.Role.USER) // 기본적으로 USER 부여
                .build();

        userRepository.save(user);

        return RegisterResponseDTO.builder()
                .clositId(user.getClositId())
                .name(userRequestDto.getName())
                .email(userRequestDto.getEmail())
                .profileImage(user.getProfileImage())
                .build();

    }

    @Override
    public void deleteUser() {
        // 현재 로그인된 사용자 정보 가져오기
        User currentUser = securityUtil.getCurrentUser(); // 로그인한 사용자 (username 또는 userId 기반)

        if (currentUser == null) {
            throw new UserHandler(ErrorStatus.USER_NOT_AUTHORIZED);
        }

        User persistentUser = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        userRepository.delete(persistentUser);
    }

    @Override
    public User registerProfileImage (String imageUrl) {

        // 현재 로그인된 사용자 정보
        User currentUser = securityUtil.getCurrentUser();

        // 사용자가 프로필 이미지를 삭제하려는 경우
        if (imageUrl == null || imageUrl.isEmpty()) {
            log.info("file is null or empty");
            s3Service.deleteFile(currentUser.getProfileImage());
            currentUser.updateProfileImage(null);
            return currentUser;
        }

        // 기존 프로필 이미지 삭제
        if (currentUser.getProfileImage() != null && !currentUser.getProfileImage().equals(defaultProfileImage)) {
            s3Service.deleteFile(currentUser.getProfileImage());
        }

        currentUser.updateProfileImage(imageUrl);

        return currentUser;
    }

    @Override
    public boolean isClositIdUnique(String clositId) {
        Optional<User> user = userRepository.findByClositId(clositId);
        return user.isEmpty();
    }

    @Override
    public User updateUserInfo(UserRequestDTO.UpdateUserDTO updateUserDTO) {

        User currentUser = securityUtil.getCurrentUser();
        Boolean isChanged = false;

        if (updateUserDTO.getName() != null) {
            currentUser.setName(updateUserDTO.getName());
            isChanged = true;
        }

        if (!passwordEncoder.matches(updateUserDTO.getCurrentPassword(), currentUser.getPassword())) {
            throw new UserHandler(ErrorStatus.INVALID_PASSWORD);
        } else {
            if (updateUserDTO.getPassword() != null) {
                currentUser.updatePassword(passwordEncoder.encode(updateUserDTO.getPassword()));
                isChanged = true;
            }
        }

        if (updateUserDTO.getBirth() != null) {
            currentUser.setBirth(updateUserDTO.getBirth());
            isChanged = true;
        }

        if (!isChanged) {
            throw new UserHandler(ErrorStatus.NO_CHANGE_DETECTED);
        }

        return currentUser;
    }

    @Override
    public UserResponseDTO.UserBlockResponseDTO blockUser (UserRequestDTO.BlockUserDTO blockUserDTO) {
        String blockedClositId = blockUserDTO.getBlockedClositId();

        User blocker = securityUtil.getCurrentUser();
        User blocked = userRepository.findByClositId(blockedClositId).orElseThrow(
                () -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        // Throw if there already exist block record
        if (blockRepository.existsByBlockerIdAndBlockedId(blocker.getClositId(), blocked.getClositId())) {
            throw new UserHandler(ErrorStatus.USER_ALREADY_BLOCKED);
        }

        // 차단된 사용자가 차단한 사용자를 팔로우 했을 때 팔로우 관계 삭제
        Follow followBlockedtoBlocker = followRepository.findBySenderAndReceiver(blocked, blocker);
        if (followBlockedtoBlocker != null) {
            followRepository.delete(followBlockedtoBlocker);
        }

        Follow followBlockertoBlocked = followRepository.findBySenderAndReceiver(blocker, blocked);
        if (followBlockertoBlocked != null) {
            followRepository.delete(followBlockertoBlocked);
        }

        Block userBlock = Block.builder()
                .blockerId(blocker.getClositId())
                .blockedId(blockUserDTO.getBlockedClositId())
                .build();

        return UserConverter.toUserBlockResponseDTO(blockRepository.save(userBlock));
    }

    @Override
    public boolean isBlockedBy(String targetClositId, String requesterClositId) {
        User targetUser = userRepository.findByClositId(targetClositId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        User requesterUser = userRepository.findByClositId(requesterClositId).orElseThrow(
                () -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        // targetClositId와 requesterClositId가 존재하는 지 확인
        if (targetUser == null || requesterUser == null) {
            throw new UserHandler(ErrorStatus.USER_NOT_FOUND);
        }

        return blockRepository.existsByBlockerIdAndBlockedId(targetClositId, requesterClositId);
    }

    @Override
    public void unblockUser (UserRequestDTO.BlockUserDTO blockUserDTO) {
        String blockedClositId = blockUserDTO.getBlockedClositId();

        User blocker = securityUtil.getCurrentUser();
        User blocked = userRepository.findByClositId(blockedClositId).orElseThrow(
                () -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        Block userBlock = blockRepository.findByBlockerIdAndBlockedId(blocker.getClositId(), blocked.getClositId())
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_BLOCKED));

        blockRepository.delete(userBlock);
    }

    @Override
    public void deactivateUser(UserRequestDTO.DeactivateUserDTO deactivateUserDTO) {
        User currentUser = securityUtil.getCurrentUser();
        User user = userRepository.findByClositId(deactivateUserDTO.getClositId())
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        // 관리자 계정만 비활성화 가능
        if (currentUser.getRole() != ADMIN) {
            throw new UserHandler(ErrorStatus.USER_NOT_AUTHORIZED);
        }

        user.deactivate();
    }
}
