package UMC_7th.Closit.domain.post.entity;

import UMC_7th.Closit.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PostItemTag extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_item_tag_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "item_tag_id")
    private ItemTag itemTag;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @Column(nullable = false)
    private Double itemTagX;

    @Column(nullable = false)
    private Double itemTagY;

    @Column(nullable = false)
    private String tagType; // FRONT or BACK
}
