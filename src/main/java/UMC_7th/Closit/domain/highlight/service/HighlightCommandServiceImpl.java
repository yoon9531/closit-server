package UMC_7th.Closit.domain.highlight.service;

import UMC_7th.Closit.domain.highlight.converter.HighlightConverter;
import UMC_7th.Closit.domain.highlight.dto.HighlightRequestDTO;
import UMC_7th.Closit.domain.highlight.entity.Highlight;
import UMC_7th.Closit.domain.highlight.repository.HighlightRepository;
import UMC_7th.Closit.domain.post.entity.Post;
import UMC_7th.Closit.domain.post.repository.PostRepository;
import UMC_7th.Closit.domain.user.entity.Role;
import UMC_7th.Closit.domain.user.entity.User;
import UMC_7th.Closit.global.apiPayload.code.status.ErrorStatus;
import UMC_7th.Closit.global.apiPayload.exception.GeneralException;
import UMC_7th.Closit.global.apiPayload.exception.handler.PostHandler;
import UMC_7th.Closit.global.apiPayload.exception.handler.UserHandler;
import UMC_7th.Closit.global.apiPayload.exception.handler.HighlightHandler;
import UMC_7th.Closit.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HighlightCommandServiceImpl implements HighlightCommandService {

    private final HighlightRepository highlightRepository;
    private final PostRepository postRepository;
    private final SecurityUtil securityUtil;

    @Override
    @Transactional
    public Highlight createHighlight(HighlightRequestDTO.CreateHighlightDTO request) {
        // 현재 로그인된 사용자 정보 가져오기
        User user = securityUtil.getCurrentUser();

        Post post = postRepository.findById(request.getPost())
                .orElseThrow(() -> new PostHandler(ErrorStatus.POST_NOT_FOUND));
        
        // 자신이 작성한 게시글이어야 함
        if (post.getUser().getId() != user.getId()) {
            throw new GeneralException(ErrorStatus.USER_NOT_AUTHORIZED);
        }

        if (highlightRepository.findByPostId(post.getId()).isPresent()) {
            throw new GeneralException(ErrorStatus.HIGHLIGHT_ALREADY_EXIST);
        }

        Highlight newHighlight = HighlightConverter.toHighlight(request, user, post);

        return highlightRepository.save(newHighlight);
    }

    @Override
    @Transactional
    public void deleteHighlight(Long postId) {
        // 현재 로그인된 사용자 정보 가져오기
        User user = securityUtil.getCurrentUser();

        Highlight highlight = highlightRepository.findByPostId(postId)
                .orElseThrow(() -> new HighlightHandler(ErrorStatus.HIGHLIGHT_NOT_FOUND));

        // 자기 자신이거나 관리자 권한이 있는 경우만 허용
        if (!user.getId().equals(highlight.getUser().getId()) &&
                !user.getRole().equals(Role.ADMIN)) {
            throw new UserHandler(ErrorStatus.USER_NOT_AUTHORIZED);
        }

        highlightRepository.delete(highlight);
    }
}
