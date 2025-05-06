package UMC_7th.Closit.domain.emailtoken.repository;

import UMC_7th.Closit.domain.emailtoken.entity.EmailToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailTokenRepository extends JpaRepository<EmailToken, Long> {

    Optional<EmailToken> findByToken(String token);

    Optional<EmailToken> findTopByEmailOrderByCreatedAtDesc(String email);
}
