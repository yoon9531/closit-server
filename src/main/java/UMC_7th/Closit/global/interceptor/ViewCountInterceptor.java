package UMC_7th.Closit.global.interceptor;

import UMC_7th.Closit.domain.battle.service.BattleService.BattleQueryService;
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
    private static final Pattern pattern = Pattern.compile("^/api/v1/communities/battle/(\\d+)(/)?$");

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String uri = request.getRequestURI();
        Matcher matcher = pattern.matcher(uri);
        if (matcher.find()) {
            Long battleId = Long.valueOf(matcher.group(1));
            handleBattleViewCookie(battleId, request, response);
        }
        return true;
    }

    private void handleBattleViewCookie(Long battleId, HttpServletRequest request, HttpServletResponse response) {
        Cookie oldCookie = null;
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("viewCount")) {
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
            Cookie newCookie = new Cookie("viewCount", "[" + battleId + "]");
            newCookie.setPath("/");
            newCookie.setMaxAge(60 * 60 * 24);
            response.addCookie(newCookie);
        }
    }
}
