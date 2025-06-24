package UMC_7th.Closit.domain.post.init;

import UMC_7th.Closit.domain.post.entity.Hashtag;
import UMC_7th.Closit.domain.post.entity.Post;
import UMC_7th.Closit.domain.post.entity.PostHashtag;
import UMC_7th.Closit.domain.post.repository.HashtagRepository;
import UMC_7th.Closit.domain.post.repository.PostHashtagRepository;
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
@Order(4)
@DummyDataInit
public class PostHashtagInit implements ApplicationRunner {

    private final PostRepository postRepository;
    private final HashtagRepository hashtagRepository;
    private final PostHashtagRepository postHashtagRepository;

    @Override
    public void run(ApplicationArguments args) {
        if (postHashtagRepository.count() > 0) {
            log.info("[PostHashtag] 더미 데이터 존재");
        } else {
            savePostHashtags();
        }
    }

    private void savePostHashtags() {
        List<Post> posts = postRepository.findAll();
        List<Hashtag> hashtags = hashtagRepository.findAll();

        List<PostHashtag> postHashtags = new ArrayList<>();

        postHashtags.add(PostHashtag.builder()
                .post(posts.get(0))
                .hashtag(hashtags.get(0))
                .build());

        postHashtags.add(PostHashtag.builder()
                .post(posts.get(0))
                .hashtag(hashtags.get(4))
                .build());

        postHashtags.add(PostHashtag.builder()
                .post(posts.get(1))
                .hashtag(hashtags.get(1))
                .build());

        postHashtags.add(PostHashtag.builder()
                .post(posts.get(1))
                .hashtag(hashtags.get(3))
                .build());

        postHashtags.add(PostHashtag.builder()
                .post(posts.get(2))
                .hashtag(hashtags.get(2))
                .build());

        postHashtags.add(PostHashtag.builder()
                .post(posts.get(3))
                .hashtag(hashtags.get(3))
                .build());

        postHashtags.add(PostHashtag.builder()
                .post(posts.get(4))
                .hashtag(hashtags.get(1))
                .build());

        postHashtags.add(PostHashtag.builder()
                .post(posts.get(4))
                .hashtag(hashtags.get(4))
                .build());

        postHashtagRepository.saveAll(postHashtags);
    }
}
