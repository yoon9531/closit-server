package UMC_7th.Closit.domain.user.service;

import UMC_7th.Closit.domain.user.entity.User;
import UMC_7th.Closit.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserWithdrawalScheduler {
    private final UserRepository userRepository;

    // 매일 새벽 3시에 실행 (cron: 초 분 시 일 월 요일)
    @Scheduled(cron = "0 0 3 * * *")
    @Transactional
    public void deleteExpiredWithdrawnUsers() {
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);
        List<User> expiredUsers = userRepository.findByIsWithdrawnTrueAndWithdrawalRequestedAtBefore(sevenDaysAgo);
        if (!expiredUsers.isEmpty()) {
            log.info("[UserWithdrawalScheduler] {}명의 탈퇴 유예 만료 계정 삭제", expiredUsers.size());
            userRepository.deleteAll(expiredUsers);
        }
    }
}
