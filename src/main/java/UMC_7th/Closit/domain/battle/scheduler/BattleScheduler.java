package UMC_7th.Closit.domain.battle.scheduler;

import UMC_7th.Closit.domain.battle.service.BattleService.BattleCommandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BattleScheduler {

    private final BattleCommandService battleCommandService;

    // 매일 자정마다 실행
    @Scheduled(cron = "0 0 0 * * *")
    public void updateBattleStatus() {
        battleCommandService.completeBattle();
    }
}
