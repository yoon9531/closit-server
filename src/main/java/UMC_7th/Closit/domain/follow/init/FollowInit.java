package UMC_7th.Closit.domain.follow.init;

import UMC_7th.Closit.domain.follow.entity.Follow;
import UMC_7th.Closit.domain.follow.repository.FollowRepository;
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
@Order(9)
@DummyDataInit
public class FollowInit implements ApplicationRunner {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    @Override
    public void run(ApplicationArguments args) {
        if (followRepository.count() > 0) {
            log.info("[Follow] 더미 데이터 존재");
        } else {
            saveFollows();
        }
    }

    private void saveFollows() {
        User user1 = userRepository.findById(1L)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        User user2 = userRepository.findById(2L)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        User user3 = userRepository.findById(3L)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        User user4 = userRepository.findById(4L)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        User user5 = userRepository.findById(5L)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        List<Follow> follows = new ArrayList<>();

        // dummy1 follows 2,3,4,5
        follows.add(Follow.builder().sender(user1).receiver(user2).build());
        follows.add(Follow.builder().sender(user1).receiver(user3).build());
        follows.add(Follow.builder().sender(user1).receiver(user4).build());
        follows.add(Follow.builder().sender(user1).receiver(user5).build());

        // dummy2 follows 1,3,4
        follows.add(Follow.builder().sender(user2).receiver(user1).build());
        follows.add(Follow.builder().sender(user2).receiver(user3).build());
        follows.add(Follow.builder().sender(user2).receiver(user4).build());

        // dummy3 follows 1,2
        follows.add(Follow.builder().sender(user3).receiver(user1).build());
        follows.add(Follow.builder().sender(user3).receiver(user2).build());

        // dummy4 follows 1,5
        follows.add(Follow.builder().sender(user4).receiver(user1).build());
        follows.add(Follow.builder().sender(user4).receiver(user5).build());

        // dummy5 follows 1,2,3
        follows.add(Follow.builder().sender(user5).receiver(user1).build());
        follows.add(Follow.builder().sender(user5).receiver(user2).build());
        follows.add(Follow.builder().sender(user5).receiver(user3).build());

        followRepository.saveAll(follows);
    }
}
