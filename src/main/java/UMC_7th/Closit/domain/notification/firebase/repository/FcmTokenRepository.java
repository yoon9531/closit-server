package UMC_7th.Closit.domain.notification.firebase.repository;

import UMC_7th.Closit.domain.notification.firebase.domain.FcmToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FcmTokenRepository extends JpaRepository<FcmToken, Long> {
}
