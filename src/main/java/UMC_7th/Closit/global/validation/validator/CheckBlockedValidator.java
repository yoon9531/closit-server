package UMC_7th.Closit.global.validation.validator;

import UMC_7th.Closit.domain.user.repository.UserRepository;
import UMC_7th.Closit.domain.user.service.UserCommandService;
import UMC_7th.Closit.global.validation.annotation.CheckBlocked;
import UMC_7th.Closit.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class CheckBlockedValidator {

    private final UserCommandService userService;
    private final SecurityUtil securityUtil;

    @Before("@annotation(checkBlocked)")
    public void validateBlockAccess(JoinPoint joinPoint, CheckBlocked checkBlocked) {
        String targetClositId = getString(joinPoint, checkBlocked);

        // 현재 로그인한 사용자 ID
        String requesterClositId = securityUtil.getCurrentUser().getClositId();
        // 차단 여부 검사
        if (userService.isBlockedBy(targetClositId, requesterClositId)) {
            log.warn("차단된 사용자 접근 차단: [{}] is blocked by [{}]", requesterClositId, targetClositId);
            throw new AccessDeniedException("You are blocked by this user.");
        }
    }

    private static String getString (JoinPoint joinPoint, CheckBlocked checkBlocked) {
        String paramName = checkBlocked.targetIdParam();

        // 파라미터 이름과 값을 매칭
        Object[] args = joinPoint.getArgs();
        String[] paramNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();

        String targetClositId = null;
        for (int i = 0; i < paramNames.length; i++) {
            if (paramNames[i].equals(paramName)) {
                targetClositId = String.valueOf(args[i]);
                break;
            }
        }

        if (targetClositId == null) {
            throw new IllegalArgumentException("Parameter '" + paramName + "' not found in method arguments.");
        }
        return targetClositId;
    }
}
