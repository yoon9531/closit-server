package UMC_7th.Closit.domain.user.repository;

import UMC_7th.Closit.domain.user.entity.TokenBlackList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenBlackListRepository extends JpaRepository<TokenBlackList, Long> {

    boolean existsByAccessToken(String accessToken);

    void deleteByAccessToken(String accessToken);
}
