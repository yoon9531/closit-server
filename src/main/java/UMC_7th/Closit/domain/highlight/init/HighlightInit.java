package UMC_7th.Closit.domain.highlight.init;

import UMC_7th.Closit.domain.highlight.entity.Highlight;
import UMC_7th.Closit.domain.highlight.repository.HighlightRepository;
import UMC_7th.Closit.domain.post.entity.Post;
import UMC_7th.Closit.domain.post.repository.PostRepository;
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
        List<Post> posts = postRepository.findAll();

        List<Highlight> highlights = new ArrayList<>();

        highlights.add(Highlight.builder().post(posts.get(0)).build());
        highlights.add(Highlight.builder().post(posts.get(1)).build());
        highlights.add(Highlight.builder().post(posts.get(2)).build());

        highlightRepository.saveAll(highlights);
    }
}
