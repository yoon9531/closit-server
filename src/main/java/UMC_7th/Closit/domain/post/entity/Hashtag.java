package UMC_7th.Closit.domain.post.entity;

import UMC_7th.Closit.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(indexes = {
        @Index(name = "idx_hashtag_content", columnList = "content") // B-Tree 인덱스 추가
})
public class Hashtag extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hashtag_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String content;

    @OneToMany(mappedBy = "hashtag", cascade = CascadeType.ALL)
    @Builder.Default
    private List<PostHashtag> postHashtagList = new ArrayList<>();
}
