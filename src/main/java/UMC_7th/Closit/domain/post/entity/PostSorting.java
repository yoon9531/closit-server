package UMC_7th.Closit.domain.post.entity;

import org.springframework.data.domain.Sort;

public enum PostSorting {
    LATEST(Sort.by(Sort.Direction.DESC, "createdAt")),
    VIEW(Sort.by(Sort.Direction.DESC, "view"));

    private final Sort sort;

    PostSorting(Sort sort) {
        this.sort = sort;
    }

    public Sort getSort() {
        return sort;
    }
}

