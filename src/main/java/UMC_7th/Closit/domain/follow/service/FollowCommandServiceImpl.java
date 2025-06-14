package UMC_7th.Closit.domain.follow.service;

import UMC_7th.Closit.domain.follow.repository.FollowRepository;
import UMC_7th.Closit.domain.follow.converter.FollowConverter;
import UMC_7th.Closit.domain.follow.dto.FollowRequestDTO;
import UMC_7th.Closit.domain.follow.entity.Follow;
import UMC_7th.Closit.domain.notification.service.NotiCommandService;
import UMC_7th.Closit.domain.user.entity.User;
import UMC_7th.Closit.domain.user.repository.UserRepository;
import UMC_7th.Closit.global.apiPayload.code.status.ErrorStatus;
import UMC_7th.Closit.global.apiPayload.exception.GeneralException;
import UMC_7th.Closit.global.apiPayload.exception.handler.FollowHandler;
import UMC_7th.Closit.global.apiPayload.exception.handler.UserHandler;
import UMC_7th.Closit.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FollowCommandServiceImpl implements FollowCommandService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;
    private final NotiCommandService notiCommandService;
    private final SecurityUtil securityUtil;

    @Override
    @Transactional
    public Follow createFollow(FollowRequestDTO.CreateFollowDTO request) {
        // 현재 로그인된 사용자 정보를 sender로 가져오기
        String senderClositId = securityUtil.getCurrentUser().getClositId();
        String receiverClositId = request.getReceiverClositId();

        // 자기 자신은 팔로우 할 수 없음
        if (receiverClositId.equals(senderClositId)) {
            throw new GeneralException(ErrorStatus.FOLLOW_SELF_NOT_ALLOWED);
        }

        User sender = userRepository.findByClositId(senderClositId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        User receiver = userRepository.findByClositId(receiverClositId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        try {
            Follow newFollow = FollowConverter.toFollow(request, sender, receiver);

            // 팔로우 알림
            notiCommandService.followNotification(newFollow);

            return followRepository.save(newFollow);
        } catch (DataIntegrityViolationException e) {
            // 이미 팔로우한 사용자를 팔로우 할 수 없음 ({"sender_id", "receiver_id"}가 unique)
            throw new GeneralException(ErrorStatus.FOLLOW_ALREADY_EXIST);
        }
    }

    @Override
    @Transactional
    public boolean isFollowing(String receiverClositId) {
        // 현재 로그인된 사용자 정보를 sender로 가져오기
        String senderClositId = securityUtil.getCurrentUser().getClositId();

        userRepository.findByClositId(receiverClositId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        Optional<Follow> follow = followRepository.findBySenderClositIdAndReceiverClositId(senderClositId, receiverClositId);
        return follow.isPresent();
    }

    @Override
    @Transactional
    public void deleteFollow(String receiverClositId) {
        // 현재 로그인된 사용자 정보를 sender로 가져오기
        String senderClositId = securityUtil.getCurrentUser().getClositId();

        userRepository.findByClositId(receiverClositId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        Follow follow = followRepository.findBySenderClositIdAndReceiverClositId(senderClositId, receiverClositId)
                .orElseThrow(() -> new FollowHandler(ErrorStatus.FOLLOW_NOT_FOUND));

        followRepository.delete(follow);
    }
}
