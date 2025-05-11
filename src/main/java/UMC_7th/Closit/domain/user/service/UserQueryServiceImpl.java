package UMC_7th.Closit.domain.user.service;

import UMC_7th.Closit.domain.follow.entity.Follow;
import UMC_7th.Closit.domain.follow.repository.FollowRepository;
import UMC_7th.Closit.domain.highlight.entity.Highlight;
import UMC_7th.Closit.domain.highlight.repository.HighlightRepository;
import UMC_7th.Closit.domain.post.entity.Post;
import UMC_7th.Closit.domain.post.repository.PostRepository;
import UMC_7th.Closit.domain.user.converter.UserConverter;
import UMC_7th.Closit.domain.user.dto.UserResponseDTO;
import UMC_7th.Closit.domain.user.entity.User;
import UMC_7th.Closit.domain.user.repository.BlockRepository;
import UMC_7th.Closit.domain.user.repository.UserRepository;
import UMC_7th.Closit.global.apiPayload.code.status.ErrorStatus;
import UMC_7th.Closit.global.apiPayload.exception.handler.UserHandler;
import UMC_7th.Closit.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserQueryServiceImpl implements UserQueryService {

    private final UserRepository userRepository;
    private final HighlightRepository highlightRepository;
    private final FollowRepository followRepository;
    private final PostRepository postRepository;
    private final SecurityUtil securityUtil;
    private final BlockRepository userBlockRepository;

    @Override
    public Slice<Highlight> getHighlightList(String clositId, Pageable pageable) {
        User user = userRepository.findByClositId(clositId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        return highlightRepository.findAllByUser(user, pageable);
    }

    @Override
    public Slice<User> getFollowerList(String clositId, Pageable pageable) {
        User user = userRepository.findByClositId(clositId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        Slice<Follow> followers = followRepository.findByReceiver(user, pageable);
        return followers.map(Follow::getSender);
    }

    @Override
    public Slice<User> getFollowingList(String clositId, Pageable pageable) {
        User user = userRepository.findByClositId(clositId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        Slice<Follow> followings = followRepository.findBySender(user, pageable);
        return followings.map(Follow::getReceiver);
    }

    @Override
    public UserResponseDTO.UpdateUserInfoDTO getUserInfo(String clositId) {
        User user = userRepository.findByClositId(clositId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        long followerCount = followRepository.countByReceiver(user);
        long followingCount = followRepository.countBySender(user);

        return UserConverter.toUpdateUserInfoDTO(user, followerCount, followingCount);
    }

    @Override
    public boolean isMissionDone() {
        // 현재 로그인된 사용자 정보 가져오기
        User user = securityUtil.getCurrentUser();

        LocalDateTime startOfDay = LocalDateTime.now().toLocalDate().atStartOfDay();
        LocalDateTime now = LocalDateTime.now();

        return !postRepository.findAllByUserIdAndCreatedAtBetween(user.getId(), startOfDay, now).isEmpty();

    }

    @Override
    public Slice<Post> getRecentPostList(String clositId, Integer page) { // 특정 사용자의 최근 게시글 조회
        Pageable pageable = PageRequest.of(page, 10);

        LocalDateTime week = LocalDateTime.now().minusWeeks(1);

        Slice<Post> recentPostList = postRepository.findRecentPostList(clositId, week, pageable);

        return recentPostList;
    }

    @Override
    public Slice<User> getBlockedUserList(Pageable pageable) {
        User currentUser = securityUtil.getCurrentUser();
        return userBlockRepository.findBlockedUsersByBlocker(currentUser.getClositId(), pageable);
    }

    @Override
    public UserResponseDTO.IsBlockedDTO isBlockedBy(String targetClositId) {

        User targetUser = userRepository.findByClositId(targetClositId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        User currentUser = securityUtil.getCurrentUser();

        UserResponseDTO.IsBlockedDTO isblockedDTO = UserResponseDTO.IsBlockedDTO.builder()
                .isBlocked(userBlockRepository.existsByBlockerIdAndBlockedId(targetClositId, currentUser.getClositId()))
                .build();

        return isblockedDTO;
    }
}
