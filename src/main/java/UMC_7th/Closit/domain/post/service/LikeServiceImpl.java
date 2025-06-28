package UMC_7th.Closit.domain.post.service;

import UMC_7th.Closit.domain.notification.service.NotiCommandService;
import UMC_7th.Closit.domain.post.converter.LikeConverter;
import UMC_7th.Closit.domain.post.dto.LikeResponseDTO;
import UMC_7th.Closit.domain.post.entity.Likes;
import UMC_7th.Closit.domain.post.entity.Post;
import UMC_7th.Closit.domain.post.repository.LikeRepository;
import UMC_7th.Closit.domain.post.repository.PostRepository;
import UMC_7th.Closit.domain.user.entity.User;
import UMC_7th.Closit.domain.user.repository.UserRepository;
import UMC_7th.Closit.global.apiPayload.code.status.ErrorStatus;
import UMC_7th.Closit.global.apiPayload.exception.GeneralException;
import UMC_7th.Closit.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {
    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final NotiCommandService notiCommandService;
    private final SecurityUtil securityUtil;

    @Override
    public LikeResponseDTO.LikeStatusDTO likePost(Long postId) {
        // 현재 로그인된 사용자 정보 가져오기
        User user = userRepository.findById(securityUtil.getCurrentUser().getId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.POST_NOT_FOUND));

        if (likeRepository.existsByUserAndPost(user, post)) {
            throw new GeneralException(ErrorStatus.LIKES_ALREADY_EXIST);
        }

        Likes likes = Likes.createLikes(user, post);
        likeRepository.save(likes);

        // 좋아요 알림
        notiCommandService.likeNotification(likes);

        return LikeConverter.toLikeStatusDTO(post, user, true);
    }

    @Override
    public LikeResponseDTO.LikeStatusDTO unlikePost(Long postId) {
        // 현재 로그인된 사용자 정보 가져오기
        User user = securityUtil.getCurrentUser();

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.POST_NOT_FOUND));

        Likes like = likeRepository.findByUserAndPost(user, post)
                .orElseThrow(() -> new GeneralException(ErrorStatus.LIKES_NOT_FOUND));

        likeRepository.delete(like);

        return LikeConverter.toLikeStatusDTO(post, user, false);
    }

}
