package UMC_7th.Closit.domain.post.init;

import UMC_7th.Closit.domain.post.entity.ItemTag;
import UMC_7th.Closit.domain.post.entity.Post;
import UMC_7th.Closit.domain.post.entity.PostItemTag;
import UMC_7th.Closit.domain.post.repository.ItemTagRepository;
import UMC_7th.Closit.domain.post.repository.PostItemTagRepository;
import UMC_7th.Closit.domain.post.repository.PostRepository;
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
@Order(6)
@DummyDataInit
public class PostItemTagInit implements ApplicationRunner {

    private final PostRepository postRepository;
    private final ItemTagRepository itemTagRepository;
    private final PostItemTagRepository postItemTagRepository;

    @Override
    public void run(ApplicationArguments args) {
        if (postItemTagRepository.count() > 0) {
            log.info("[PostItemTag] 더미 데이터 존재");
        } else {
            savePostItemTags();
        }
    }

    private void savePostItemTags() {
        List<Post> posts = postRepository.findAll();
        List<ItemTag> itemTags = itemTagRepository.findAll();

        // 최소 개수 확인
        if (posts.size() < 5) {
            throw new GeneralException(ErrorStatus._BAD_REQUEST);
        }
        if (itemTags.size() < 5) {
            throw new GeneralException(ErrorStatus._BAD_REQUEST);
        }

        List<PostItemTag> postItemTags = new ArrayList<>();

        postItemTags.add(PostItemTag.builder()
                .post(posts.get(0))
                .itemTag(itemTags.get(0))
                .itemTagX(0.2)
                .itemTagY(0.3)
                .tagType("FRONT")
                .build());

        postItemTags.add(PostItemTag.builder()
                .post(posts.get(0))
                .itemTag(itemTags.get(1))
                .itemTagX(0.5)
                .itemTagY(0.4)
                .tagType("BACK")
                .build());

        postItemTags.add(PostItemTag.builder()
                .post(posts.get(1))
                .itemTag(itemTags.get(1))
                .itemTagX(0.5)
                .itemTagY(0.4)
                .tagType("BACK")
                .build());

        postItemTags.add(PostItemTag.builder()
                .post(posts.get(2))
                .itemTag(itemTags.get(2))
                .itemTagX(0.3)
                .itemTagY(0.7)
                .tagType("FRONT")
                .build());

        postItemTags.add(PostItemTag.builder()
                .post(posts.get(3))
                .itemTag(itemTags.get(3))
                .itemTagX(0.6)
                .itemTagY(0.2)
                .tagType("BACK")
                .build());

        postItemTags.add(PostItemTag.builder()
                .post(posts.get(3))
                .itemTag(itemTags.get(0))
                .itemTagX(0.2)
                .itemTagY(0.3)
                .tagType("FRONT")
                .build());

        postItemTags.add(PostItemTag.builder()
                .post(posts.get(4))
                .itemTag(itemTags.get(4))
                .itemTagX(0.1)
                .itemTagY(0.5)
                .tagType("FRONT")
                .build());

        postItemTags.add(PostItemTag.builder()
                .post(posts.get(4))
                .itemTag(itemTags.get(1))
                .itemTagX(0.5)
                .itemTagY(0.4)
                .tagType("BACK")
                .build());

        postItemTagRepository.saveAll(postItemTags);
    }
}
