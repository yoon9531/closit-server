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
import UMC_7th.Closit.global.infra.s3.S3Service;
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
    private final UserUtil userUtil;

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
        User currentUser = securityUtil.getCurrentUser();

        if (currentUser == null) {
            throw new UserHandler(ErrorStatus.USER_NOT_AUTHORIZED);
        }

        User persistentUser = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        persistentUser.setWithdrawn(true);
        persistentUser.setWithdrawalRequestedAt(java.time.LocalDateTime.now());
        userRepository.save(persistentUser);
    }

    @Override
    public User registerProfileImage (String imageUrl) {
        User currentUser = securityUtil.getCurrentUser();

        // 사용자가 프로필 이미지를 삭제하려는 경우
        if (imageUrl == null || imageUrl.isEmpty()) {
            s3Service.deleteFile(currentUser.getProfileImage());
            currentUser.updateProfileImage(defaultProfileImage);
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

        if (!passwordEncoder.matches(updateUserDTO.getPassword(), currentUser.getPassword())) {
            throw new UserHandler(ErrorStatus.INVALID_PASSWORD);
        }

        boolean isChanged = false;

        if (updateUserDTO.getName() != null) {
            currentUser.setName(updateUserDTO.getName());
            isChanged = true;
        }
        if (updateUserDTO.getPassword() != null) {
            currentUser.updatePassword(passwordEncoder.encode(updateUserDTO.getPassword()));
            isChanged = true;
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
        User blocked = userUtil.getUserByClositIdOrThrow(blockedClositId);

        validateBlockOperation(blocker, blocked);
        removeFollowRelationships(blocked, blocker);

        Block userBlock = Block.builder()
                .blockerId(blocker.getClositId())
                .blockedId(blockUserDTO.getBlockedClositId())
                .build();

        return UserConverter.toUserBlockResponseDTO(blockRepository.save(userBlock));
    }

    @Override
    public boolean isBlockedBy(String targetClositId, String requesterClositId) {
        if (!userRepository.existsByClositId(targetClositId) || !userRepository.existsByClositId(requesterClositId)) {
            throw new UserHandler(ErrorStatus.USER_NOT_FOUND);
        }

        return blockRepository.existsByBlockerIdAndBlockedId(targetClositId, requesterClositId);
    }


    @Override
    public void unblockUser (UserRequestDTO.BlockUserDTO blockUserDTO) {
        String blockedClositId = blockUserDTO.getBlockedClositId();

        User blocker = securityUtil.getCurrentUser();
        User blocked = userUtil.getUserByClositIdOrThrow(blockedClositId);

        Block userBlock = blockRepository.findByBlockerIdAndBlockedId(blocker.getClositId(), blocked.getClositId())
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_BLOCKED));

        blockRepository.delete(userBlock);
    }

    @Override
    public void cancelWithdrawal () {
        User currentUser = securityUtil.getCurrentUser();
        User persistentUser = userUtil.getUserByClositIdOrThrow(currentUser.getClositId());

        if (persistentUser.getWithdrawalRequestedAt().plusDays(7).isBefore(java.time.LocalDateTime.now())) {
            throw new UserHandler(ErrorStatus.WITHDRAWAL_PERIOD_EXPIRED);
        }

        persistentUser.setWithdrawn(false);
        persistentUser.setWithdrawalRequestedAt(null);
        userRepository.save(persistentUser);
    }

    @Override
    public void deactivateUser(UserRequestDTO.DeactivateUserDTO deactivateUserDTO) {
        User currentUser = securityUtil.getCurrentUser();
        User user = userUtil.getUserByClositIdOrThrow(deactivateUserDTO.getClositId());

        // 관리자 계정만 비활성화 가능
        if (currentUser.getRole() != ADMIN) {
            throw new UserHandler(ErrorStatus.USER_NOT_AUTHORIZED);
        }

        user.deactivate();
    }

    // Helper Methods

    private void removeFollowRelationships(User blocked, User blocker) {
        Follow followBlockedtoBlocker = followRepository.findBySenderAndReceiver(blocked, blocker);
        if (followBlockedtoBlocker != null) {
            followRepository.delete(followBlockedtoBlocker);
        }

        Follow followBlockertoBlocked = followRepository.findBySenderAndReceiver(blocker, blocked);
        if (followBlockertoBlocked != null) {
            followRepository.delete(followBlockertoBlocked);
        }
    }

    private void validateBlockOperation(User blocker, User blocked) {
        if (blockRepository.existsByBlockerIdAndBlockedId(blocker.getClositId(), blocked.getClositId())) {
            throw new UserHandler(ErrorStatus.USER_ALREADY_BLOCKED);
        }
    }
}
