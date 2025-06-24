package UMC_7th.Closit.domain.user.repository.init;

import UMC_7th.Closit.domain.user.entity.Role;
import UMC_7th.Closit.domain.user.entity.User;
import UMC_7th.Closit.domain.user.repository.UserRepository;
import UMC_7th.Closit.global.util.DummyDataInit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Order(0)
@DummyDataInit
public class UserInit implements ApplicationRunner {

    private final UserRepository userRepository;

    @Override
    public void run(ApplicationArguments args) {
        if (userRepository.count() > 0) {
            log.info("[User] 더미 데이터 존재");
        } else {
            saveUser();
        }
    }

    private void saveUser() {
        List<User> users = new ArrayList<>();

        User user1 = User.builder()
                .name("dummy1")
                .clositId("dummy1")
                .email("dummy1@gmail.com")
                .password("123456")
                .profileImage("https://closit-bucket.s3.ap-northeast-2.amazonaws.com/default-user-profile.png")
                .isWithdrawn(false)
                .birth(LocalDate.EPOCH)
                .role(Role.USER)
                .countReport(0)
                .build();

        User user2 = User.builder()
                .name("dummy2")
                .clositId("dummy2")
                .email("dummy2@gmail.com")
                .password("123456")
                .profileImage("https://closit-bucket.s3.ap-northeast-2.amazonaws.com/default-user-profile.png")
                .isWithdrawn(false)
                .birth(LocalDate.EPOCH)
                .role(Role.USER)
                .countReport(0)
                .build();

        User user3 = User.builder()
                .name("dummy3")
                .clositId("dummy3")
                .email("dummy3@gmail.com")
                .password("123456")
                .profileImage("https://closit-bucket.s3.ap-northeast-2.amazonaws.com/default-user-profile.png")
                .isWithdrawn(false)
                .birth(LocalDate.EPOCH)
                .role(Role.USER)
                .countReport(0)
                .build();

        users.add(user1);
        users.add(user2);
        users.add(user3);

        userRepository.saveAll(users);
    }
}
