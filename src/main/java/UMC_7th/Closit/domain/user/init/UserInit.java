package UMC_7th.Closit.domain.user.init;

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
                .password("$2a$10$m2o4cOjvdoSEj0WrlKXiGOxyeGGdNH93Vga./LW0QPWvhQjzD1Ci2") // 12345678의 해시값
                .profileImage("https://closit-bucket.s3.ap-northeast-2.amazonaws.com/default-user-profile.png")
                .isWithdrawn(false)
                .isActive(true)
                .birth(LocalDate.EPOCH)
                .role(Role.USER)
                .countReport(0)
                .build();

        User user2 = User.builder()
                .name("dummy2")
                .clositId("dummy2")
                .email("dummy2@gmail.com")
                .password("$2a$10$m2o4cOjvdoSEj0WrlKXiGOxyeGGdNH93Vga./LW0QPWvhQjzD1Ci2") // 12345678의 해시값
                .profileImage("https://closit-bucket.s3.ap-northeast-2.amazonaws.com/default-user-profile.png")
                .isWithdrawn(false)
                .isActive(true)
                .birth(LocalDate.EPOCH)
                .role(Role.USER)
                .countReport(0)
                .build();

        User user3 = User.builder()
                .name("dummy3")
                .clositId("dummy3")
                .email("dummy3@gmail.com")
                .password("$2a$10$m2o4cOjvdoSEj0WrlKXiGOxyeGGdNH93Vga./LW0QPWvhQjzD1Ci2") // 12345678의 해시값
                .profileImage("https://closit-bucket.s3.ap-northeast-2.amazonaws.com/default-user-profile.png")
                .isWithdrawn(false)
                .isActive(true)
                .birth(LocalDate.EPOCH)
                .role(Role.USER)
                .countReport(0)
                .build();

        User user4 = User.builder()
                .name("dummy4")
                .clositId("dummy4")
                .email("dummy4@gmail.com")
                .password("$2a$10$m2o4cOjvdoSEj0WrlKXiGOxyeGGdNH93Vga./LW0QPWvhQjzD1Ci2") // 12345678의 해시값
                .profileImage("https://closit-bucket.s3.ap-northeast-2.amazonaws.com/default-user-profile.png")
                .isWithdrawn(false)
                .isActive(true)
                .birth(LocalDate.EPOCH)
                .role(Role.USER)
                .countReport(0)
                .build();

        User user5 = User.builder()
                .name("dummy5")
                .clositId("dummy5")
                .email("dummy5@gmail.com")
                .password("$2a$10$m2o4cOjvdoSEj0WrlKXiGOxyeGGdNH93Vga./LW0QPWvhQjzD1Ci2") // 12345678의 해시값
                .profileImage("https://closit-bucket.s3.ap-northeast-2.amazonaws.com/default-user-profile.png")
                .isWithdrawn(false)
                .isActive(true)
                .birth(LocalDate.EPOCH)
                .role(Role.USER)
                .countReport(0)
                .build();

        users.add(user1);
        users.add(user2);
        users.add(user3);
        users.add(user4);
        users.add(user5);

        userRepository.saveAll(users);
    }
}
