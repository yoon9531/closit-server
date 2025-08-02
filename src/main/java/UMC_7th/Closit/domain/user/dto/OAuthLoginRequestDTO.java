package UMC_7th.Closit.domain.user.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OAuthLoginRequestDTO {

    /**
     * 토큰 타입 (ID_TOKEN 또는 ACCESS_TOKEN)
     */
    @NotNull(message = "토큰 타입은 필수입니다.")
    private TokenType tokenType;

    /**
     * 소셜 플랫폼에서 발급받은 토큰
     */
    @NotBlank(message = "토큰은 필수입니다.")
    private String token;

    /**
     * 토큰 타입 열거형
     */
    public enum TokenType {
        ID_TOKEN,       // ID Token (OpenID Connect)
        ACCESS_TOKEN    // Access Token (OAuth 2.0)
    }

    // 기존 호환성을 위한 생성자 (deprecated)
    @Deprecated
    public OAuthLoginRequestDTO(String idToken) {
        this.tokenType = TokenType.ID_TOKEN;
        this.token = idToken;
    }

    // 기존 호환성을 위한 getter (deprecated)
    @Deprecated
    public String getIdToken() {
        return tokenType == TokenType.ID_TOKEN ? token : null;
    }
}
