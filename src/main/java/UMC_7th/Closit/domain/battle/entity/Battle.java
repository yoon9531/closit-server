package UMC_7th.Closit.domain.battle.entity;

import UMC_7th.Closit.domain.battle.entity.enums.BattleStatus;
import UMC_7th.Closit.domain.post.entity.Post;
import UMC_7th.Closit.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Battle extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "battle_id")
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @Column
    private LocalDateTime deadline;

    @Column(name = "first_voting_cnt")
    private int firstVotingCnt = 0;

    @Column(name = "second_voting_cnt")
    private int secondVotingCnt = 0;

    @Column(name = "like_count")
    private int likeCount = 0;

    @Column(name = "view_count")
    private int viewCount = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "battle_status", nullable = false)
    private BattleStatus battleStatus;

    @OneToMany(mappedBy = "battle", cascade = CascadeType.ALL)
    private List<BattleLike> battleLikesList = new ArrayList<>();

    @OneToMany(mappedBy = "battle", cascade = CascadeType.ALL)
    private List<BattleComment> battleCommentList = new ArrayList<>();

    @OneToMany(mappedBy = "battle", cascade = CascadeType.ALL)
    private List<Vote> voteList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id1")
    private Post post1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id2")
    private Post post2;

    @Builder
    public Battle(
            String title,
            String description,
            LocalDateTime deadline,
            int firstVotingCnt,
            int secondVotingCnt,
            int likeCount,
            int viewCount,
            BattleStatus battleStatus,
            Post post1,
            Post post2
    ) {
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.firstVotingCnt = firstVotingCnt;
        this.secondVotingCnt = secondVotingCnt;
        this.likeCount = likeCount;
        this.viewCount = viewCount;
        this.battleStatus = battleStatus;
        this.post1 = post1;
        this.post2 = post2;
    }

    public void challengeBattle() { // 배틀 신청
        this.battleStatus = BattleStatus.PENDING;
    }

    public void acceptChallenge(Post post2, LocalDateTime deadline) { // 배틀 수락
        this.post2 = post2;
        this.battleStatus = BattleStatus.ACTIVE;
        this.deadline = deadline;
    }

    public void completeBattle() { // 배틀 스케줄러 - 진행 상태 변경
        this.battleStatus = BattleStatus.COMPLETED;
    }

    public boolean availableVote () {
        return LocalDateTime.now().isAfter(deadline);
    }

    public void updateVotingCnt(int firstVotingCnt, int secondVotingCnt) { // 배틀 게시글 목록 조회
        this.firstVotingCnt = firstVotingCnt;
        this.secondVotingCnt = secondVotingCnt;
    }
}