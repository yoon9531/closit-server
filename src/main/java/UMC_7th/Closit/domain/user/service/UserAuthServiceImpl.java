package UMC_7th.Closit.domain.user.service;

import UMC_7th.Closit.domain.emailtoken.service.EmailTokenService;
import UMC_7th.Closit.domain.user.converter.UserConverter;
import UMC_7th.Closit.domain.user.dto.JwtResponse;
import UMC_7th.Closit.domain.user.dto.LoginRequestDTO;
import UMC_7th.Closit.domain.user.dto.OAuthLoginRequestDTO;
import UMC_7th.Closit.domain.user.dto.UserResponseDTO;
import UMC_7th.Closit.domain.user.entity.*;
import UMC_7th.Closit.domain.user.repository.RefreshTokenRepository;
import UMC_7th.Closit.domain.user.repository.TokenBlackListRepository;
import UMC_7th.Closit.domain.user.repository.UserRepository;
import UMC_7th.Closit.global.apiPayload.code.status.ErrorStatus;
import UMC_7th.Closit.global.apiPayload.exception.GeneralException;
import UMC_7th.Closit.global.apiPayload.exception.handler.JwtHandler;
import UMC_7th.Closit.global.apiPayload.exception.handler.UserHandler;
import UMC_7th.Closit.global.common.SocialLoginType;
import UMC_7th.Closit.security.jwt.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserAuthServiceImpl implements UserAuthService {

    private final UserRepository userRepository;
    private final EmailTokenService emailTokenService;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;
    private final GoogleOAuthService googleOAuthService;
    private final TokenBlackListRepository tokenBlackListRepository;
    private final UserUtil userUtil;

    @Value("${cloud.aws.s3.default-profile-image}")
    private String defaultProfileImage;

    @Override
    public JwtResponse login(LoginRequestDTO loginRequestDto) {
        User user = userUtil.getUserByClositIdOrThrow(loginRequestDto.getClositId());

        if (!passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
            throw new UserHandler(ErrorStatus.PASSWORD_NOT_CORRESPOND);
        }

        if (!user.getIsActive()) {
            throw new UserHandler(ErrorStatus.USER_NOT_ACTIVE);
        }

        return userUtil.issueJwtTokens(user);
    }

    @Override
    public UserResponseDTO.UserInfoDTO updateUserRole (String clositId, Role newRole) {
        User user = userUtil.getUserByClositIdOrThrow(clositId);
        User updatedUser = user.updateRole(newRole);

        return UserConverter.toUserInfoDTO(updatedUser);
    }

    @Override
    public JwtResponse refresh(String refreshToken) {
        jwtTokenProvider.validateToken(refreshToken);
        Claims claims = jwtTokenProvider.getClaims(refreshToken);
        String clositId = claims.getSubject();

        User user = userUtil.getUserByClositIdOrThrow(clositId);

        RefreshToken savedToken = refreshTokenRepository.findByUsername(clositId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        if (!savedToken.getRefreshToken().trim().equals(refreshToken.trim())) {
            throw new JwtHandler(ErrorStatus.INVALID_REFRESH_TOKEN);
        }

        return userUtil.issueJwtTokens(user);
    }

    @Override
    public String findClositIdByEmail(String email) {
        if (!emailTokenService.isEmailVerified(email)) {
            throw new UserHandler(ErrorStatus.EMAIL_NOT_VERIFIED);
        }

        User user = userUtil.getUserByEmailOrThrow(email);

        emailTokenService.markTokenAsUsed(email);

        return user.getClositId();
    }

    @Override
    public void resetPassword(String email, String newPassword) {
        if (!emailTokenService.isEmailVerified(email)) {
            throw new UserHandler(ErrorStatus.EMAIL_NOT_VERIFIED);
        }

        User user = userUtil.getUserByEmailOrThrow(email);

        String encodedPassword = passwordEncoder.encode(newPassword);
        user.updatePassword(encodedPassword);

        // 인증 토큰 사용 처리
        emailTokenService.markTokenAsUsed(email);
    }

    @Override
    public JwtResponse socialLogin (SocialLoginType socialLoginType, OAuthLoginRequestDTO oauthLoginRequestDTO) {
        String idToken = oauthLoginRequestDTO.getIdToken();
        OAuthUserInfo userInfo;

        if (socialLoginType == SocialLoginType.GOOGLE) { // GOOGLE
            userInfo = googleOAuthService.getUserInfo(idToken);
        } else {
            throw new GeneralException(ErrorStatus.NOT_SUPPORTED_SOCIAL_LOGIN);
        }

        User user = userRepository.findByEmail(userInfo.getEmail())
                .orElseGet(() -> registerNewUser(userInfo));

        return userUtil.issueJwtTokens(user);
    }

    @Override
    public void logout (String accessToken) {
        Claims claims = jwtTokenProvider.getClaims(accessToken);
        String clositId = claims.getSubject();

        RefreshToken refreshToken = refreshTokenRepository.findByUsername(clositId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        blacklistAndRemoveRefreshToken(accessToken, refreshToken);
    }

    // Helper Methods
    private void blacklistAndRemoveRefreshToken(String accessToken, RefreshToken refreshToken) {
        TokenBlackList blackedToken = TokenBlackList.builder()
                .accessToken(accessToken)
                .clositId(refreshToken.getUsername())
                .build();

        tokenBlackListRepository.save(blackedToken);
        refreshTokenRepository.delete(refreshToken);
    }

    private User registerNewUser(OAuthUserInfo userInfo) {
        String email = userInfo.getEmail();
        String name = userInfo.getName();

        if (userRepository.existsByEmail(email)) {
            throw new UserHandler(ErrorStatus.USER_ALREADY_EXIST);
        }

        String clositId = userUtil.generateUniqueClositId(userInfo);

        // 더미 비밀번호 생성
        String dummyPassword = UUID.randomUUID().toString();
        String encodedPassword = passwordEncoder.encode(dummyPassword);


        User newUser = User.builder()
                .email(email)
                .name(name)
                .profileImage(defaultProfileImage)
                .password(encodedPassword)
                .provider(userInfo.getProvider().toString())
                .clositId(clositId)
                .role(Role.USER)
                .countReport(0)
                .build();

        return userRepository.save(newUser);
    }

}