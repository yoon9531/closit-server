package UMC_7th.Closit.domain.user.repository;

import UMC_7th.Closit.domain.user.entity.User;
import UMC_7th.Closit.domain.user.entity.UserBlock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserBlockRepository extends JpaRepository<UserBlock, Long> {
    boolean existsByBlockerAndBlocked(User blocker, User blocked);
}
