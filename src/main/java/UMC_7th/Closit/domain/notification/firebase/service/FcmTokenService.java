package UMC_7th.Closit.domain.notification.firebase.service;

import UMC_7th.Closit.domain.notification.firebase.converter.FcmTokenConverter;
import UMC_7th.Closit.domain.notification.firebase.domain.FcmToken;
import UMC_7th.Closit.domain.notification.firebase.dto.FcmTokenRequest;
import UMC_7th.Closit.domain.notification.firebase.repository.FcmTokenRepository;
import UMC_7th.Closit.domain.user.entity.User;
import UMC_7th.Closit.domain.user.repository.UserRepository;
import UMC_7th.Closit.global.apiPayload.code.status.ErrorStatus;
import UMC_7th.Closit.global.apiPayload.exception.GeneralException;
import UMC_7th.Closit.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FcmTokenService {

    private final SecurityUtil securityUtil;
    private final UserRepository userRepository;
    private final FcmTokenRepository fcmTokenRepository;

    @Transactional
    public void getFcmToken(FcmTokenRequest request) {
        User user = securityUtil.getCurrentUser();

        if (!userRepository.existsById(user.getId())) {
            throw new GeneralException(ErrorStatus.USER_NOT_FOUND);
        }
        FcmToken fcmToken = FcmTokenConverter.toEntity(request, user);

        fcmTokenRepository.save(fcmToken);
    }
}
