package UMC_7th.Closit.domain.user.repository;

import UMC_7th.Closit.domain.user.entity.User;
import UMC_7th.Closit.domain.user.entity.Block;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BlockRepository extends JpaRepository<Block, Long> {
    boolean existsByBlockerIdAndBlockedId(String blocker, String blocked);

    Optional<Block> findByBlockerIdAndBlockedId (String blockerId, String blockedId);

    @Query("SELECT b.blockedId FROM Block b WHERE b.blockerId = :blockerId")
    Slice<User> findBlockedUsersByBlocker(@Param("blockerId") String blockerId, Pageable pageable);

}
