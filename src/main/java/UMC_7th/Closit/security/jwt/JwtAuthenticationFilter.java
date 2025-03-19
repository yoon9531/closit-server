package UMC_7th.Closit.security.jwt;

import UMC_7th.Closit.domain.user.entity.Role;
import UMC_7th.Closit.domain.user.entity.User;
import UMC_7th.Closit.domain.user.repository.UserRepository;
import UMC_7th.Closit.domain.user.service.CustomUserDetailService;
import UMC_7th.Closit.global.apiPayload.code.status.ErrorStatus;
import UMC_7th.Closit.global.apiPayload.exception.handler.UserHandler;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailService customUserDetailsService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal (
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String token = resolveToken(request);


        if (token != null && jwtTokenProvider.validateToken(token)) {
            Claims claims = jwtTokenProvider.getClaims(token);
            String email = claims.getSubject();
            String roleString = claims.get("role", String.class);

            Role role = Role.valueOf(roleString); // String->Role 반환

            // Blocked User
            User user = userRepository.findByEmail(email).orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));
            if (user.getRole() == Role.BLOCKED) {
                log.warn("Blocked user [{}] attempted to access secured resources.", email);
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"Access is denied.\"}");
                return;
            }

            UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);

            // SecurityContext에 UserDetails 설정
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, // principal을 UserDetails 객체로 설정
                    null,
                    List.of(new SimpleGrantedAuthority("ROLE_"+role.name())) // authorities를 SimpleGrantedAuthority 객체로 설정
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

        }

        filterChain.doFilter(request, response);
    }


    private String resolveToken (HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

}
