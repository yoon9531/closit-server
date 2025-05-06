package UMC_7th.Closit.domain.post.service;

import UMC_7th.Closit.domain.follow.entity.Follow;
import UMC_7th.Closit.domain.follow.repository.FollowRepository;
import UMC_7th.Closit.domain.highlight.repository.HighlightRepository;
import UMC_7th.Closit.domain.post.converter.PostConverter;
import UMC_7th.Closit.domain.post.dto.PostResponseDTO;
import UMC_7th.Closit.domain.post.entity.Hashtag;
import UMC_7th.Closit.domain.post.entity.ItemTag;
import UMC_7th.Closit.domain.post.entity.Post;
import UMC_7th.Closit.domain.post.repository.*;
import UMC_7th.Closit.domain.user.entity.User;
import UMC_7th.Closit.global.apiPayload.code.status.ErrorStatus;
import UMC_7th.Closit.global.apiPayload.exception.GeneralException;
import UMC_7th.Closit.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostQueryServiceImpl implements PostQueryService {

    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    private final BookmarkRepository bookmarkRepository;
    private final FollowRepository followRepository;
    private final HashTagRepository hashtagRepository;
    private final PostHashTagRepository postHashTagRepository;
    private final ItemTagRepository itemTagRepository;
    private final SecurityUtil securityUtil;
    private final HighlightRepository highlightRepository;

    @Transactional // ← readOnly 제거 또는 false 설정
    public PostResponseDTO.PostPreviewDTO getPostById(Long postId) {
        User currentUser = securityUtil.getCurrentUser();

        // 게시글 조회
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new GeneralException(ErrorStatus._NOT_FOUND));

        // 조회수 증가
        post.increaseView();

        // 좋아요 여부 확인
        Boolean isLiked = likeRepository.existsByUserAndPost(currentUser, post);

        // 북마크 여부 확인
        Boolean isSaved = bookmarkRepository.existsByUserAndPost(currentUser, post);

        // 하이라이트 여부 확인
        Boolean isHighlighted = highlightRepository.existsByPost(post);

        // 해시태그 조회
        List<String> hashtags = postHashTagRepository.findByPost(post).stream()
                .map(postHashtag -> postHashtag.getHashtag().getContent())
                .collect(Collectors.toList());

        // 아이템 태그 조회
        List<ItemTag> frontTags = itemTagRepository.findByPostAndTagType(post, "FRONT");
        List<ItemTag> backTags = itemTagRepository.findByPostAndTagType(post, "BACK");

        // DTO로 변환
        return PostConverter.toPostPreviewDTO(post, isLiked, isSaved, isHighlighted, hashtags, frontTags, backTags);
    }

    public Slice<PostResponseDTO.PostPreviewDTO> getPostListByFollowing(boolean follower, Pageable pageable) {
        User currentUser = securityUtil.getCurrentUser();

        Slice<Post> posts;
        if (follower) {
            // `follower == true` → 팔로우한 유저들의 게시글 조회
            List<User> followingUsers = followRepository.findBySender(currentUser).stream()
                    .map(Follow::getReceiver)
                    .collect(Collectors.toList());

            posts = postRepository.findByUsers(followingUsers, pageable);
        } else {
            // `follower == false` → 모든 사용자의 최신 게시글 조회
            posts = postRepository.findAllByOrderByCreatedAtDesc(pageable);
        }

        return posts.map(post -> {
            // 좋아요 여부
            Boolean isLiked = likeRepository.existsByUserAndPost(currentUser, post);

            // 북마크 여부
            Boolean isSaved = bookmarkRepository.existsByUserAndPost(currentUser, post);

            // 하이라이트 여부 확인
            Boolean isHighlighted = highlightRepository.existsByPost(post);

            // 해시태그 조회
            List<String> hashtags = postHashTagRepository.findByPost(post).stream()
                    .map(postHashtag -> postHashtag.getHashtag().getContent())
                    .collect(Collectors.toList());

            // 아이템 태그 조회
            List<ItemTag> frontTags = itemTagRepository.findByPostAndTagType(post, "FRONT");
            List<ItemTag> backTags = itemTagRepository.findByPostAndTagType(post, "BACK");

            // DTO 변환
            return PostConverter.toPostPreviewDTO(post, isLiked, isSaved, isHighlighted, hashtags, frontTags, backTags);
        });
    }

    public Slice<PostResponseDTO.PostPreviewDTO> getPostListByHashtag(String hashtag, Pageable pageable) {
        if (hashtag == null || hashtag.isBlank()) {
            throw new GeneralException(ErrorStatus.HASHTAG_NOT_FOUND);
        }

        // 해당 해시태그가 존재하는지 확인
        Hashtag foundHashtag = hashtagRepository.findByContent(hashtag)
                .orElseThrow(() -> new GeneralException(ErrorStatus.HASHTAG_NOT_FOUND));

        // 해당 해시태그가 포함된 게시글 조회
        Slice<Post> posts = postRepository.findByHashtagId(foundHashtag.getId(), pageable);

        User currentUser = securityUtil.getCurrentUser();
        return posts.map(post -> {
            // 좋아요 여부
            Boolean isLiked = likeRepository.existsByUserAndPost(currentUser, post);

            // 북마크 여부
            Boolean isSaved = bookmarkRepository.existsByUserAndPost(currentUser, post);

            // 하이라이트 여부 확인
            Boolean isHighlighted = highlightRepository.existsByPost(post);

            // 해시태그 조회
            List<String> hashtags = postHashTagRepository.findByPost(post).stream()
                    .map(postHashtag -> postHashtag.getHashtag().getContent())
                    .collect(Collectors.toList());

            // 아이템 태그 조회
            List<ItemTag> frontTags = itemTagRepository.findByPostAndTagType(post, "FRONT");
            List<ItemTag> backTags = itemTagRepository.findByPostAndTagType(post, "BACK");

            // DTO 변환
            return PostConverter.toPostPreviewDTO(post, isLiked, isSaved, isHighlighted, hashtags, frontTags, backTags);
        });
    }

    private Slice<PostResponseDTO.PostPreviewDTO> convertToPostPreviewDTO(Slice<Post> posts, User currentUser) {
        return posts.map(post -> {
            // 좋아요 여부
            Boolean isLiked = likeRepository.existsByUserAndPost(currentUser, post);

            // 북마크 여부
            Boolean isSaved = bookmarkRepository.existsByUserAndPost(currentUser, post);

            // 하이라이트 여부 확인
            Boolean isHighlighted = highlightRepository.existsByPost(post);

            // 해시태그 조회
            List<String> hashtags = postHashTagRepository.findByPost(post).stream()
                    .map(postHashtag -> postHashtag.getHashtag().getContent())
                    .collect(Collectors.toList());

            // 아이템 태그 조회
            List<ItemTag> frontTags = itemTagRepository.findByPostAndTagType(post, "FRONT");
            List<ItemTag> backTags = itemTagRepository.findByPostAndTagType(post, "BACK");

            // DTO 변환
            return PostConverter.toPostPreviewDTO(post, isLiked, isSaved, isHighlighted, hashtags, frontTags, backTags);
        });
    }
}
