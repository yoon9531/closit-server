package UMC_7th.Closit.domain.post.init;

import UMC_7th.Closit.domain.post.entity.Hashtag;
import UMC_7th.Closit.domain.post.repository.HashtagRepository;
import UMC_7th.Closit.global.util.DummyDataInit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Order(2)
@DummyDataInit
public class HashtagInit implements ApplicationRunner {

    private final HashtagRepository hashtagRepository;

    @Override
    public void run(ApplicationArguments args) {
        if (hashtagRepository.count() > 0) {
            log.info("[Hashtag] 더미 데이터 존재");
        } else {
            saveHashtags();
        }
    }

    private void saveHashtags() {
        List<Hashtag> hashtags = List.of(
                Hashtag.builder().content("데일리룩").build(),
                Hashtag.builder().content("여름코디").build(),
                Hashtag.builder().content("시험기간룩").build(),
                Hashtag.builder().content("심플").build(),
                Hashtag.builder().content("깔끔한").build()
        );

        hashtagRepository.saveAll(hashtags);
    }
}
