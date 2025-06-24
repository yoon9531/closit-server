package UMC_7th.Closit.domain.battle.init;

import UMC_7th.Closit.domain.battle.entity.Battle;
import UMC_7th.Closit.domain.battle.entity.ChallengeBattle;
import UMC_7th.Closit.domain.battle.entity.enums.ChallengeStatus;
import UMC_7th.Closit.domain.battle.repository.BattleRepository;
import UMC_7th.Closit.domain.battle.repository.ChallengeBattleRepository;
import UMC_7th.Closit.domain.post.entity.Post;
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
@Order(7)
@DummyDataInit
public class ChallengeBattleInit implements ApplicationRunner {

    private final PostRepository postRepository;
    private final BattleRepository battleRepository;
    private final ChallengeBattleRepository challengeBattleRepository;

    @Override
    public void run(ApplicationArguments args) {
        if (challengeBattleRepository.count() > 0) {
            log.info("[ChallengeBattle] 더미 데이터 존재");
        } else {
            saveChallengeBattle();
        }
    }

    private void saveChallengeBattle() {
        Post post1 = postRepository.findById(2L)
                .orElseThrow(() -> new GeneralException(ErrorStatus.POST_NOT_FOUND));

        Post post2 = postRepository.findById(3L)
                .orElseThrow(() -> new GeneralException(ErrorStatus.POST_NOT_FOUND));

        Battle battle1 = battleRepository.findById(2L)
                .orElseThrow(() -> new GeneralException(ErrorStatus.BATTLE_NOT_FOUND));

        Battle battle2 = battleRepository.findById(3L)
                .orElseThrow(() -> new GeneralException(ErrorStatus.BATTLE_NOT_FOUND));


        List<ChallengeBattle> challengeBattles = new ArrayList<>();

        ChallengeBattle challengeBattle1 = ChallengeBattle.builder()
                .battle(battle2)
                .post(post2)
                .challengeStatus(ChallengeStatus.PENDING)
                .build();

        ChallengeBattle challengeBattle2 = ChallengeBattle.builder()
                .battle(battle1)
                .post(post1)
                .challengeStatus(ChallengeStatus.REJECTED)
                .build();

        ChallengeBattle challengeBattle3 = ChallengeBattle.builder()
                .battle(battle1)
                .post(post2)
                .challengeStatus(ChallengeStatus.ACCEPTED)
                .build();

        challengeBattles.add(challengeBattle1);
        challengeBattles.add(challengeBattle2);
        challengeBattles.add(challengeBattle3);

        challengeBattleRepository.saveAll(challengeBattles);
    }
}
