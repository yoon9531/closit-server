package UMC_7th.Closit.security.jwt;

import UMC_7th.Closit.global.apiPayload.code.status.ErrorStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public void commence (HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {

        ErrorStatus errorStatus = ErrorStatus.USER_NOT_LOGGED_IN;

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(errorStatus.getReasonHttpStatus().getHttpStatus().value());

        String formattedTimestamp = LocalDateTime.now().format(FORMATTER);

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("code", errorStatus.getCode());
        errorResponse.put("message", errorStatus.getMessage());
        errorResponse.put("timestamp", formattedTimestamp);

        String jsonResponse = OBJECT_MAPPER.writeValueAsString(errorResponse);
        response.getWriter().write(jsonResponse);
    }
}
