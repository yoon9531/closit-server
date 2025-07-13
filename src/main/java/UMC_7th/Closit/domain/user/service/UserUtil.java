package UMC_7th.Closit.domain.user.service;

import UMC_7th.Closit.domain.user.dto.JwtResponse;
import UMC_7th.Closit.domain.user.entity.OAuthUserInfo;
import UMC_7th.Closit.domain.user.entity.RefreshToken;
import UMC_7th.Closit.domain.user.entity.Role;
import UMC_7th.Closit.domain.user.entity.User;
import UMC_7th.Closit.domain.user.repository.RefreshTokenRepository;
import UMC_7th.Closit.domain.user.repository.UserRepository;
import UMC_7th.Closit.global.apiPayload.code.status.ErrorStatus;
import UMC_7th.Closit.global.apiPayload.exception.handler.UserHandler;
import UMC_7th.Closit.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserUtil {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;


    @Value("${cloud.aws.s3.default-profile-image}")
    private String defaultProfileImage;

    // ClositId로 사용자 조회
    public User getUserByClositIdOrThrow(String clositId) {
        return userRepository.findByClositId(clositId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));
    }

    // 이메일로 사용자 조회
    public User getUserByEmailOrThrow(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));
    }

    public JwtResponse issueJwtTokens(User user) {
        String subject = user.getClositId();
        String accessToken = jwtTokenProvider.createAccessToken(subject, user.getRole());
        String refreshToken = jwtTokenProvider.createRefreshToken(subject, user.getRole());

        refreshTokenRepository.save(
                refreshTokenRepository.findByUsername(subject)
                        .map(rt -> {
                            rt.updateRefreshToken(refreshToken);
                            return rt;
                        })
                        .orElse(new RefreshToken(subject, refreshToken))
        );

        return new JwtResponse(user.getClositId(), accessToken, refreshToken);
    }


    // 소셜 로그인 : clositId 생성
    public String generateUniqueClositId(OAuthUserInfo userInfo) {
        String base = userInfo.getName().replaceAll("\\s+", "").toLowerCase();
        String suffix;
        String randomClositId;

        do {
            suffix = UUID.randomUUID().toString().substring(0, 6);
            randomClositId = base + "_" + suffix;
        } while(userRepository.existsByClositId(randomClositId));

        return randomClositId;
    }

    // 새로운 사용자 등록
    public User registerNewUser(OAuthUserInfo userInfo) {
        if (userRepository.existsByEmail(userInfo.getEmail())) {
            throw new UserHandler(ErrorStatus.USER_ALREADY_EXIST);
        }
        String clositId = generateUniqueClositId(userInfo);
        String dummyPassword = passwordEncoder.encode(UUID.randomUUID().toString());
        User newUser = User.builder()
                .email(userInfo.getEmail())
                .name(userInfo.getName())
                .profileImage(defaultProfileImage)
                .password(dummyPassword)
                .provider(userInfo.getProvider().toString())
                .clositId(clositId)
                .role(Role.USER)
                .countReport(0)
                .build();

        return userRepository.save(newUser);
    }

}
