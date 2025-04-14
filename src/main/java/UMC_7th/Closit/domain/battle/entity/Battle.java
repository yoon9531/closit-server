package UMC_7th.Closit.domain.battle.entity;

import UMC_7th.Closit.domain.post.entity.Post;
import UMC_7th.Closit.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Battle extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "battle_id")
    private Long id;

    @Column
    private String title;

    @Column
    private LocalDate deadline;

    @Column
    @Builder.Default
    private Integer firstVotingCnt = 0;

    @Column
    @Builder.Default
    private Integer secondVotingCnt = 0;

    @Transient
    private double firstVotingRate;

    @Transient
    private double secondVotingRate;

    @Column
    @Builder.Default
    private Integer likeCount = 0;

    @Column
    @Builder.Default
    private Integer view = 0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BattleStatus battleStatus;

    @OneToMany(mappedBy = "battle", cascade = CascadeType.ALL)
    @Builder.Default
    private List<BattleLike> battleLikesList = new ArrayList<>();

    @OneToMany(mappedBy = "battle", cascade = CascadeType.ALL)
    @Builder.Default
    private List<BattleComment> battleCommentList = new ArrayList<>();

    @OneToMany(mappedBy = "battle", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Vote> voteList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id1")
    private Post post1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id2")
    private Post post2;

    public void challengeBattle() { // 배틀 신청
        this.battleStatus = BattleStatus.PENDING;
    }

    public void acceptChallenge(Post post2, LocalDate deadline) { // 배틀 수락
        this.post2 = post2;
        this.battleStatus = BattleStatus.ACTIVE;
        this.deadline = deadline;
    }

    public boolean availableVote () {
        return LocalDate.now().isAfter(deadline);
    }

    public void incrementFirstVotingCnt() { // 첫 번째 게시글 투표
        this.firstVotingCnt++;
    }

    public void incrementSecondVotingCnt() { // 두 번째 게시글 투표
        this.secondVotingCnt++;
    }

    public void updateVotingCnt(Integer firstVotingCnt, Integer secondVotingCnt) { // 배틀 게시글 목록 조회
        this.firstVotingCnt = firstVotingCnt;
        this.secondVotingCnt = secondVotingCnt;
    }

    public void updateVotingRate (double firstVotingRate, double secondVotingRate) {
        this.firstVotingRate = firstVotingRate;
        this.secondVotingRate = secondVotingRate;
    }

    public void increaseLikeCount() { // 배틀 좋아요 생성
        if (this.likeCount == null) {
            this.likeCount = 1;
        } else {
            this.likeCount++;
        }
    }

    public void decreaseLikeCount() { // 배틀 좋아요 삭제
        if (this.likeCount == null) {
            this.likeCount = 0;
        } else {
            this.likeCount--;
        }
    }
}