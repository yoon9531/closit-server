package UMC_7th.Closit.security.jwt;

import UMC_7th.Closit.domain.user.entity.Role;
import UMC_7th.Closit.global.apiPayload.code.status.ErrorStatus;
import UMC_7th.Closit.global.apiPayload.exception.handler.JwtHandler;
import UMC_7th.Closit.global.apiPayload.exception.handler.UserHandler;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;

@Component
@Slf4j
public class JwtTokenProvider {

    private static final String BEARER_PREFIX = "Bearer ";
    private static final String ROLE_CLAIM = "role";
    private static final String AUTHORIZATION_HEADER = "Authorization";

    private final Key key;
    private final Long accessTokenValidity;
    private final Long refreshTokenValidity;
    private final JwtParser jwtParser;

    public JwtTokenProvider(@Value("${jwt.secret-key}") String secretKey,
                            @Value("${jwt.expiration.access}") String accessTokenValidity,
                            @Value("${jwt.expiration.refresh}") String refreshTokenValidity) {
        this.key = createSigningKey(secretKey);
        this.accessTokenValidity = Long.parseLong(accessTokenValidity);
        this.refreshTokenValidity = Long.parseLong(refreshTokenValidity);
        this.jwtParser = createJwtParser();
    }

    public String createToken(String email, Role role, long validity) {
        Instant now = Instant.now();

        return Jwts.builder()
                .setSubject(email)
                .claim(ROLE_CLAIM, role)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusMillis(validity)))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String createAccessToken(String email, Role role) {
        return createToken(email, role, accessTokenValidity);
    }

    public String createRefreshToken(String email, Role role) {
        return createToken(email, role, refreshTokenValidity);
    }

    public Claims getClaims(String token) {
        try {
            return parseToken(token);
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public boolean validateToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch (ExpiredJwtException e) {
            throw new UserHandler(ErrorStatus.EXPIRED_TOKEN);
        } catch (IllegalArgumentException e) {
            throw new JwtHandler(ErrorStatus.EMPTY_TOKEN);
        } catch (JwtException e) {
            handleJwtException(e);
            return false;
        }
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

        return extractTokenFromBearer(bearerToken);
    }

    // Helper methods
    private String extractTokenFromBearer(String bearerToken) {
        if (bearerToken != null && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }
        return null;
    }

    private Claims parseToken(String token) {
        return jwtParser.parseClaimsJws(token).getBody();
    }

    private Key createSigningKey(String secretKey) {
        String base64Secret = Base64.getEncoder().encodeToString(secretKey.getBytes());
        return Keys.hmacShaKeyFor(Base64.getDecoder().decode(base64Secret));
    }

    private JwtParser createJwtParser() {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build();
    }

    private void handleJwtException(JwtException e) {
        if (e instanceof MalformedJwtException) {
            throw new JwtHandler(ErrorStatus.INVALID_TOKEN);
        } else if (e instanceof UnsupportedJwtException) {
            throw new JwtHandler(ErrorStatus.UNSUPPORTED_TOKEN);
        }else {
            throw new JwtHandler(ErrorStatus.INVALID_TOKEN);
        }
    }

}


