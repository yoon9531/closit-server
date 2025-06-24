package UMC_7th.Closit.domain.post.init;

import UMC_7th.Closit.domain.post.entity.Post;
import UMC_7th.Closit.domain.post.entity.Visibility;
import UMC_7th.Closit.domain.post.repository.PostRepository;
import UMC_7th.Closit.domain.user.entity.User;
import UMC_7th.Closit.domain.user.repository.UserRepository;
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
@Order(3)
@DummyDataInit
public class PostInit implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Override
    public void run(ApplicationArguments args) {
        if (postRepository.count() > 0) {
            log.info("[Post] 더미 데이터 존재");
        } else {
            savePost();
        }
    }

    private void savePost() {
        User user1 = userRepository.findById(1L)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        User user2 = userRepository.findById(2L)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        User user4 = userRepository.findById(4L)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        User user5 = userRepository.findById(5L)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        List<Post> posts = new ArrayList<>();

        Post post1 = Post.builder()
                .user(user1)
                .frontImage("https://closit-bucket.s3.ap-northeast-2.amazonaws.com/post/front/60c0b9af-3fad-48e0-b46e-889dd264eac0.jpg")
                .backImage("https://closit-bucket.s3.ap-northeast-2.amazonaws.com/post/back/579aa421-4f0f-4eae-bc4c-d3cf3570f8df.jpg")
                .pointColor("#ddffdd")
                .isMission(true)
                .visibility(Visibility.PUBLIC)
                .view(0)
                .build();

        Post post2 = Post.builder()
                .user(user2)
                .frontImage("https://closit-bucket.s3.ap-northeast-2.amazonaws.com/post/front/60c0b9af-3fad-48e0-b46e-889dd264eac0.jpg")
                .backImage("https://closit-bucket.s3.ap-northeast-2.amazonaws.com/post/back/579aa421-4f0f-4eae-bc4c-d3cf3570f8df.jpg")
                .pointColor("#ddffff")
                .isMission(false)
                .visibility(Visibility.PUBLIC)
                .view(5)
                .build();

        Post post3 = Post.builder()
                .user(user1)
                .frontImage("https://closit-bucket.s3.ap-northeast-2.amazonaws.com/post/front/60c0b9af-3fad-48e0-b46e-889dd264eac0.jpg")
                .backImage("https://closit-bucket.s3.ap-northeast-2.amazonaws.com/post/back/579aa421-4f0f-4eae-bc4c-d3cf3570f8df.jpg")
                .pointColor("#ddffff")
                .isMission(false)
                .visibility(Visibility.PUBLIC)
                .view(3)
                .build();

        Post post4 = Post.builder()
                .user(user4)
                .frontImage("https://closit-bucket.s3.ap-northeast-2.amazonaws.com/post/front/60c0b9af-3fad-48e0-b46e-889dd264eac0.jpg")
                .backImage("https://closit-bucket.s3.ap-northeast-2.amazonaws.com/post/back/579aa421-4f0f-4eae-bc4c-d3cf3570f8df.jpg")
                .pointColor("#ddffff")
                .isMission(false)
                .visibility(Visibility.PUBLIC)
                .view(4)
                .build();

        Post post5 = Post.builder()
                .user(user5)
                .frontImage("https://closit-bucket.s3.ap-northeast-2.amazonaws.com/post/front/60c0b9af-3fad-48e0-b46e-889dd264eac0.jpg")
                .backImage("https://closit-bucket.s3.ap-northeast-2.amazonaws.com/post/back/579aa421-4f0f-4eae-bc4c-d3cf3570f8df.jpg")
                .pointColor("#ddffff")
                .isMission(false)
                .visibility(Visibility.PUBLIC)
                .view(1)
                .build();

        posts.add(post1);
        posts.add(post2);
        posts.add(post3);
        posts.add(post4);
        posts.add(post5);

        postRepository.saveAll(posts);
    }
}
