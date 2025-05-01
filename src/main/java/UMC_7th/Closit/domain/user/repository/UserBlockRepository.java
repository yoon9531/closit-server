package UMC_7th.Closit.domain.user.repository;

import UMC_7th.Closit.domain.user.entity.User;
import UMC_7th.Closit.domain.user.entity.UserBlock;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserBlockRepository extends JpaRepository<UserBlock, Long> {
    boolean existsByBlockerAndBlocked(User blocker, User blocked);

    Optional<UserBlock> findByBlockerAndBlocked (User blocker, User blocked);

    Slice<User> findAllByBlocker(User blocker);

    @Query("SELECT ub.blocked FROM UserBlock ub WHERE ub.blocker = :blocker")
    Slice<User> findBlockedUsersByBlocker(@Param("blocker") User blocker, Pageable pageable);

}
