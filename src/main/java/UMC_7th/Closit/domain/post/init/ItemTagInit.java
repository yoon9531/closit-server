package UMC_7th.Closit.domain.post.init;

import UMC_7th.Closit.domain.post.entity.ItemTag;
import UMC_7th.Closit.domain.post.repository.ItemTagRepository;
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
public class ItemTagInit implements ApplicationRunner {

    private final ItemTagRepository itemTagRepository;

    @Override
    public void run(ApplicationArguments args) {
        if (itemTagRepository.count() > 0) {
            log.info("[ItemTag] 더미 데이터 존재");
        } else {
            saveItemTags();
        }
    }

    private void saveItemTags() {
        List<ItemTag> itemTags = List.of(
                ItemTag.builder().content("상의").build(),
                ItemTag.builder().content("하의").build(),
                ItemTag.builder().content("신발").build(),
                ItemTag.builder().content("가방").build(),
                ItemTag.builder().content("모자").build()
        );

        itemTagRepository.saveAll(itemTags);
    }
}
