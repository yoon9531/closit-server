package UMC_7th.Closit.domain.post.entity.enums;

import lombok.Getter;
import org.springframework.data.domain.Sort;

@Getter
public enum PostSorting {
    LATEST(Sort.by(Sort.Direction.DESC, "createdAt")),
    VIEW(Sort.by(Sort.Direction.DESC, "view"));

    private final Sort sort;

    PostSorting(Sort sort) {
        this.sort = sort;
    }
}
