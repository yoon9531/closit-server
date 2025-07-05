package UMC_7th.Closit.global.interceptor;

import UMC_7th.Closit.domain.battle.service.BattleService.BattleQueryService;
import UMC_7th.Closit.security.SecurityUtil;
import UMC_7th.Closit.security.jwt.JwtTokenProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class ViewCountInterceptor implements HandlerInterceptor {

    private final BattleQueryService battleQueryService;
    private final JwtTokenProvider jwtTokenProvider;
    private final SecurityUtil securityUtil;

    private static final Pattern pattern = Pattern.compile("^/api/v1/communities/battle/(\\d+)(/)?$");

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!request.getMethod().equalsIgnoreCase("GET")) {
            return true;
        }

        String uri = request.getRequestURI();
        Matcher matcher = pattern.matcher(uri);
        if (matcher.find()) {
            Long battleId = Long.valueOf(matcher.group(1));

            String token = resolveToken(request);
            if (token != null && jwtTokenProvider.validateToken(token)) {
                Long userId = securityUtil.getCurrentUser().getId();
                handleBattleViewCookie(userId, battleId, request, response);
            }
        }
        return true;
    }

    private void handleBattleViewCookie(Long userId, Long battleId, HttpServletRequest request, HttpServletResponse response) {
        Cookie oldCookie = null;
        Cookie[] cookies = request.getCookies();

        String cookieName = "userViewCount" + userId;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieName)) {
                    oldCookie = cookie;
                    break;
                }
            }
        }

        if (oldCookie != null) {
            if (!oldCookie.getValue().contains("[" + battleId + "]")) {
                battleQueryService.updateBattleView(battleId);
                oldCookie.setValue(oldCookie.getValue() + "_[" + battleId + "]");
                oldCookie.setPath("/");
                oldCookie.setMaxAge(60 * 60 * 24);
                response.addCookie(oldCookie);
            }
        } else {
            Cookie newCookie = new Cookie(cookieName, "[" + battleId + "]");
            newCookie.setPath("/");
            newCookie.setMaxAge(60 * 60 * 24);
            response.addCookie(newCookie);
        }
    }

    private String resolveToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        return (bearer != null && bearer.startsWith("Bearer ")) ? bearer.substring(7) : null;
    }
}
