package UMC_7th.Closit.domain.battle.init;

import UMC_7th.Closit.domain.battle.entity.Battle;
import UMC_7th.Closit.domain.battle.entity.enums.BattleStatus;
import UMC_7th.Closit.domain.battle.repository.BattleRepository;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Order(6)
@DummyDataInit
public class BattleInit implements ApplicationRunner {

    private final PostRepository postRepository;
    private final BattleRepository battleRepository;

    @Override
    public void run(ApplicationArguments args) {
        if (battleRepository.count() > 0) {
            log.info("[Battle] 더미 데이터 존재");
        } else {
            saveBattle();
        }
    }

    private void saveBattle() {
        Post post1 = postRepository.findById(1L)
                .orElseThrow(() -> new GeneralException(ErrorStatus.POST_NOT_FOUND));

        Post post2 = postRepository.findById(2L)
                .orElseThrow(() -> new GeneralException(ErrorStatus.POST_NOT_FOUND));

        Post post3 = postRepository.findById(3L)
                .orElseThrow(() -> new GeneralException(ErrorStatus.POST_NOT_FOUND));

        List<Battle> battles = new ArrayList<>();

        Battle battle1 = Battle.builder()
                .post1(post1)
                .post2(post2)
                .firstVotingCnt(10)
                .secondVotingCnt(2)
                .likeCount(5)
                .viewCount(60)
                .title("제목1")
                .description("설명1")
                .deadline(LocalDateTime.now().plusDays(3L))
                .battleStatus(BattleStatus.ACTIVE)
                .build();

        Battle battle2 = Battle.builder()
                .post1(post3)
                .post2(post2)
                .firstVotingCnt(1)
                .secondVotingCnt(5)
                .likeCount(1)
                .viewCount(10)
                .title("제목2")
                .description("설명2")
                .deadline(LocalDateTime.now().plusDays(3L))
                .battleStatus(BattleStatus.ACTIVE)
                .build();

        Battle battle3 = Battle.builder()
                .post1(post1)
                .firstVotingCnt(0)
                .secondVotingCnt(0)
                .likeCount(0)
                .viewCount(0)
                .title("제목3")
                .description("설명3")
                .battleStatus(BattleStatus.INACTIVE)
                .build();

        Battle battle4 = Battle.builder()
                .post1(post1)
                .post2(post3)
                .firstVotingCnt(10)
                .secondVotingCnt(6)
                .likeCount(10)
                .viewCount(60)
                .title("제목4")
                .description("설명4")
                .deadline(LocalDateTime.now())
                .battleStatus(BattleStatus.COMPLETED)
                .build();

        battles.add(battle1);
        battles.add(battle2);
        battles.add(battle3);
        battles.add(battle4);

        battleRepository.saveAll(battles);
    }
}
