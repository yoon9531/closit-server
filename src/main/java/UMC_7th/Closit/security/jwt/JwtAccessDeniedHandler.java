package UMC_7th.Closit.security.jwt;

import UMC_7th.Closit.global.apiPayload.code.status.ErrorStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public void handle (HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        ErrorStatus userNotAuthorized = ErrorStatus.USER_NOT_AUTHORIZED;
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(userNotAuthorized.getReasonHttpStatus().getHttpStatus().value());

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("code", "COMMON403");
        errorResponse.put("message", "권한이 없습니다.");
        // 클라이언트에서 이전 화면으로 이동하도록 안내할 수 있는 추가 필드
        errorResponse.put("action", "goBack");

        String jsonResponse = OBJECT_MAPPER.writeValueAsString(errorResponse);
        response.getWriter().write(jsonResponse);
    }
}
