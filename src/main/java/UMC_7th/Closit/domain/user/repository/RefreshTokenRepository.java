package UMC_7th.Closit.domain.user.repository;

import UMC_7th.Closit.domain.user.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Ref;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
    Optional<RefreshToken> findByUsername(String username);
    void deleteByUsername(String username);
}
