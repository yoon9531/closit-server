package UMC_7th.Closit.domain.highlight.service;

import UMC_7th.Closit.domain.highlight.entity.Highlight;
import UMC_7th.Closit.domain.highlight.repository.HighlightRepository;
import UMC_7th.Closit.global.apiPayload.code.status.ErrorStatus;
import UMC_7th.Closit.global.apiPayload.exception.handler.HighlightHandler;
import UMC_7th.Closit.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HighlightQueryServiceImpl implements HighlightQueryService {

    private final HighlightRepository highlightRepository;
    private final SecurityUtil securityUtil;

    @Override
    public Highlight findHighlight(Long id) {
        // 현재 로그인 상태 확인
        securityUtil.getCurrentUser();

        return highlightRepository.findByIdWithPost(id)
                .orElseThrow(() -> new HighlightHandler(ErrorStatus.HIGHLIGHT_NOT_FOUND));
    }
}
