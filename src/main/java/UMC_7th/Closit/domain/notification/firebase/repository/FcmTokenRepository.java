package UMC_7th.Closit.domain.notification.firebase.repository;

import UMC_7th.Closit.domain.notification.firebase.domain.FcmToken;
import UMC_7th.Closit.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FcmTokenRepository extends JpaRepository<FcmToken, Long> {
    List<FcmToken> findByUser(User user);
}
