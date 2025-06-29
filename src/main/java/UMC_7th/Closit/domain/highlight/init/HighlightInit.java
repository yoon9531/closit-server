package UMC_7th.Closit.domain.highlight.init;

import UMC_7th.Closit.domain.highlight.entity.Highlight;
import UMC_7th.Closit.domain.highlight.repository.HighlightRepository;
import UMC_7th.Closit.domain.post.entity.Post;
import UMC_7th.Closit.domain.post.repository.PostRepository;
import UMC_7th.Closit.domain.user.entity.User;
import UMC_7th.Closit.global.apiPayload.code.status.ErrorStatus;
import UMC_7th.Closit.global.apiPayload.exception.GeneralException;
import UMC_7th.Closit.global.util.DummyDataInit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Order(8)
@DummyDataInit
public class HighlightInit implements ApplicationRunner {

    private final PostRepository postRepository;
    private final HighlightRepository highlightRepository;

    @Override
    public void run(ApplicationArguments args) {
        if (highlightRepository.count() > 0) {
            log.info("[Highlight] 더미 데이터 존재");
        } else {
            saveHighlights();
        }
    }

    private void saveHighlights() {
        Post post1 = postRepository.findById(1L)
                .orElseThrow(() -> new GeneralException(ErrorStatus.POST_NOT_FOUND));

        Post post2 = postRepository.findById(2L)
                .orElseThrow(() -> new GeneralException(ErrorStatus.POST_NOT_FOUND));

        Post post3 = postRepository.findById(3L)
                .orElseThrow(() -> new GeneralException(ErrorStatus.POST_NOT_FOUND));

        List<Highlight> highlights = new ArrayList<>();

        highlights.add(Highlight.builder().post(post1).build());
        highlights.add(Highlight.builder().post(post2).build());
        highlights.add(Highlight.builder().post(post3).build());

        highlightRepository.saveAll(highlights);
    }
}
