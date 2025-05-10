package UMC_7th.Closit.domain.report.service;

import UMC_7th.Closit.domain.report.dto.ReportRequestDTO;
import UMC_7th.Closit.domain.report.entity.Description;
import UMC_7th.Closit.domain.report.entity.Type;
import UMC_7th.Closit.domain.report.repository.ReportRepository;
import UMC_7th.Closit.domain.user.entity.Role;
import UMC_7th.Closit.domain.user.entity.User;
import UMC_7th.Closit.domain.user.repository.UserRepository;
import UMC_7th.Closit.security.SecurityUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
class ReportServiceTest {

    @Autowired
    private ReportService reportService;

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private UserRepository userRepository;

    @MockitoBean
    private SecurityUtil securityUtil;

    private final String receiverEmail = "dummy@ex.com";
    private final String reporterEmail = "reporter@ex.com";
    private final String receiverClositId = "dummy";
    private final String reporterClositId = "reporter";
    @Test
    void reportUser_concurrent_SolvingRaceCondition() throws Exception {
        // given
        User receiver = userRepository.save(User.builder()
                .clositId("dummy123")
                .email(receiverEmail)
                .name("dummy")
                .profileImage("dummy.png")
                .password("12341234")
                .role(Role.USER)
                .countReport(0)
                .build());

        User reporter = userRepository.save(User.builder()
                .clositId("reporter")
                .email(reporterEmail)
                .name("reporter")
                .profileImage("dummy.png")
                .password("12341234")
                .role(Role.USER)
                .countReport(0)
                .build());

        when(securityUtil.getCurrentUser()).thenReturn(reporter);

        ReportRequestDTO dto = ReportRequestDTO.builder()
                .receiverClositId("dummy123")
                .type(Type.USER)
                .description(Description.SPAM)
                .build();

        int threadCount = 100;
        ExecutorService executor = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                try {
                    reportService.reportUser(dto);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        // then
        User updated = userRepository.findByClositId("dummy123").orElseThrow();
        System.out.println("\uD83D\uDCC9 최종 reportCount: " + updated.getCountReport());
        assertThat(updated.getCountReport()).isEqualTo(threadCount);
    }

    @AfterEach
    @Transactional
    void cleanUp() {
        reportRepository.deleteAll();
        userRepository.deleteByClositId(receiverClositId);
        userRepository.deleteByClositId(reporterClositId);
    }
}