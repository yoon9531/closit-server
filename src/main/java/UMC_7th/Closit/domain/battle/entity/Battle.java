package UMC_7th.Closit.domain.battle.entity;

import UMC_7th.Closit.domain.post.entity.Post;
import UMC_7th.Closit.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@DynamicUpdate
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Battle extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "battle_id")
    private Long id;

    @Column
    private String title;

    @Column(nullable = false)
    private LocalDate deadline = LocalDate.now().plusDays(3);

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

    public void setPost2 (Post post2) { // 배틀 신청
        this.post2 = post2;
    }

    @PrePersist
    public void voteDeadline() { // 배틀 투표 - 마감 기한 3일 뒤 설정
        if (this.deadline == null) {
            this.deadline = LocalDate.now().plusDays(3);
        }
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

    public void setFirstVotingCnt (Integer firstVotingCnt) { // 배틀 게시글 목록 조회
        this.firstVotingCnt = firstVotingCnt;
    }

    public void setSecondVotingCnt (Integer secondVotingCnt) {
        this.secondVotingCnt = secondVotingCnt;
    }

    public void setFirstVotingRate (double firstVotingRate) {
        this.firstVotingRate = firstVotingRate;
    }

    public void setSecondVotingRate (double secondVotingRate) {
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