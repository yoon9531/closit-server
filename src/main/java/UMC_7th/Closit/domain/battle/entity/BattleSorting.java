package UMC_7th.Closit.domain.battle.entity;

import lombok.Getter;
import org.springframework.data.domain.Sort;

@Getter
public enum BattleSorting {
    LATEST(Sort.by(Sort.Direction.DESC, "createdAt")),
    VIEW(Sort.by(Sort.Direction.DESC, "viewCount"));

    private final Sort sort;

    BattleSorting(Sort sort) {
        this.sort = sort;
    }
}
