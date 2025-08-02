package UMC_7th.Closit.domain.user.service.social;

import UMC_7th.Closit.global.apiPayload.code.status.ErrorStatus;
import UMC_7th.Closit.global.apiPayload.exception.GeneralException;
import UMC_7th.Closit.global.common.SocialLoginType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SocialLoginFactory {
    private final List<SocialLoginService> socialLoginServices;
    private Map<SocialLoginType, SocialLoginService> serviceMap;

    public void init() {
        if (serviceMap == null) {
            serviceMap = socialLoginServices.stream()
                    .collect(Collectors.toMap(
                            SocialLoginService::getSupportedType,
                            Function.identity()
                    ));
        }
    }

    /**
     * 소셜 로그인 타입에 맞는 서비스 반환
     * @param socialLoginType 소셜 로그인 타입
     * @return 해당 타입의 소셜 로그인 서비스
     */
    public SocialLoginService getService(SocialLoginType socialLoginType) {
        init();

        SocialLoginService service = serviceMap.get(socialLoginType);
        if (service == null) {
            throw new GeneralException(ErrorStatus.NOT_SUPPORTED_SOCIAL_LOGIN);
        }

        return service;
    }
}
