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
        @Index(name = "idx_item_tag_content", columnList = "content") // B-Tree 인덱스 추가
})
public class ItemTag extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_tag_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String content;

    @OneToMany(mappedBy = "itemTag", cascade = CascadeType.ALL)
    @Builder.Default
    private List<PostItemTag> postItemTags = new ArrayList<>();
}
