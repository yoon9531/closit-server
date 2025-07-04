package UMC_7th.Closit.domain.user.service;

import UMC_7th.Closit.domain.emailtoken.service.EmailTokenService;
import UMC_7th.Closit.domain.user.converter.UserConverter;
import UMC_7th.Closit.domain.user.dto.JwtResponse;
import UMC_7th.Closit.domain.user.dto.LoginRequestDTO;
import UMC_7th.Closit.domain.user.dto.OAuthLoginRequestDTO;
import UMC_7th.Closit.domain.user.dto.UserResponseDTO;
import UMC_7th.Closit.domain.user.entity.OAuthUserInfo;
import UMC_7th.Closit.domain.user.entity.RefreshToken;
import UMC_7th.Closit.domain.user.entity.Role;
import UMC_7th.Closit.domain.user.entity.User;
import UMC_7th.Closit.domain.user.repository.RefreshTokenRepository;
import UMC_7th.Closit.domain.user.repository.UserRepository;
import UMC_7th.Closit.global.apiPayload.code.status.ErrorStatus;
import UMC_7th.Closit.global.apiPayload.exception.GeneralException;
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

    // profile image 주소
    @Value("${cloud.aws.s3.default-profile-image}")
    private String profileImage;

    // login
    @Override
    public JwtResponse login(LoginRequestDTO loginRequestDto) {
        User user = userRepository.findByClositId(loginRequestDto.getClositId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        if (!passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
            throw new GeneralException(ErrorStatus.PASSWORD_NOT_CORRESPOND);
        }

        if (!user.getIsActive()) {
            throw new GeneralException(ErrorStatus.USER_NOT_ACTIVE);
        }

        String accessToken = jwtTokenProvider.createAccessToken(user.getEmail(), user.getRole());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getEmail(), user.getRole());

        refreshTokenRepository.save(new RefreshToken(user.getEmail(), refreshToken));

        return new JwtResponse(user.getClositId(),accessToken, refreshToken);
    }

    @Override
    public UserResponseDTO.UserInfoDTO updateUserRole (Long userId, Role newRole) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        User updatedUser = user.updateRole(newRole);
        return UserConverter.toUserInfoDTO(updatedUser);
    }

    @Override
    public JwtResponse refresh(String refreshToken) {
        // Refresh Token 유효성 검사
        jwtTokenProvider.validateToken(refreshToken);
        Claims claims = jwtTokenProvider.getClaims(refreshToken);
        String email = claims.getSubject();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        RefreshToken savedToken = refreshTokenRepository.findByUsername(email)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        // 저장된 Refresh Token과 비교 (공백 제거)
        if (!savedToken.getRefreshToken().trim().equals(refreshToken.trim())) {
            throw new GeneralException(ErrorStatus.INVALID_REFRESH_TOKEN);
        }

        Role role = user.getRole();

        // 새로운 Access Token, Refresh Token 생성
        String newAccessToken = jwtTokenProvider.createAccessToken(email, role);
        String newRefreshToken = jwtTokenProvider.createRefreshToken(email, role);

        // Refresh Token 업데이트
        savedToken.updateRefreshToken(newRefreshToken);
        refreshTokenRepository.save(new RefreshToken(email, newRefreshToken));

        return new JwtResponse(user.getClositId(), newAccessToken, newRefreshToken);
    }

    @Override
    public String findClositIdByEmail(String email) {
        // 이메일 인증 여부 확인
        if (!emailTokenService.isEmailVerified(email)) {
            throw new UserHandler(ErrorStatus.EMAIL_NOT_VERIFIED);
        }

        // 사용자 조회
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        // 인증 토큰 사용 처리
        emailTokenService.markTokenAsUsed(email);

        return user.getClositId();
    }

    @Override
    public void resetPassword(String email, String newPassword) {
        // 이메일 인증 여부 확인
        if (!emailTokenService.isEmailVerified(email)) {
            throw new UserHandler(ErrorStatus.EMAIL_NOT_VERIFIED);
        }

        // 사용자 조회
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        // 비밀번호 변경
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.updatePassword(encodedPassword);

        // 인증 토큰 사용 처리
        emailTokenService.markTokenAsUsed(email);
    }

    @Override
    public JwtResponse socialLogin (SocialLoginType socialLoginType, OAuthLoginRequestDTO oauthLoginRequestDTO) {
        // Client에서 전달된 idToken을 통해 유저 정보 가져오기
        String idToken = oauthLoginRequestDTO.getIdToken();
        OAuthUserInfo userInfo;


        if (socialLoginType == SocialLoginType.GOOGLE) { // GOOGLE
            userInfo = googleOAuthService.getUserInfo(idToken);
        } else {
            throw new GeneralException(ErrorStatus.NOT_SUPPORTED_SOCIAL_LOGIN);
        }

        // 이미 가입된 유저인지 확인
        User user = userRepository.findByEmail(userInfo.getEmail())
                .orElseGet(() -> registerNewUser(userInfo));


        // 토큰 생성
        String accessToken = jwtTokenProvider.createAccessToken(user.getEmail(), user.getRole());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getEmail(), user.getRole());

        refreshTokenRepository.save(new RefreshToken(user.getEmail(), refreshToken));

        return new JwtResponse(user.getClositId(), accessToken, refreshToken);
    }

    @Override
    public void logout (String accessToken) {
        jwtTokenProvider.validateToken(accessToken);
        Claims claims = jwtTokenProvider.getClaims(accessToken);
        String username = claims.getSubject();

        log.info("Logging out user: {}", username);
        // Refresh Token 삭제
        RefreshToken refreshToken = refreshTokenRepository.findByUsername(username)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        refreshTokenRepository.delete(refreshToken);

        // Access Token은 서버에서 관리하지 않으므로 별도의 처리는 필요 없음
        log.info("User {} has been logged out successfully.", username);

        // 로그아웃 후 클라이언트 측에서 Access Token을 삭제하도록 안내
        // 예: 클라이언트에서 Access Token을 삭제하는 로직을 구현해야 함
        log.info("Access Token for user {} has been invalidated.", username);
    }

    // Register User info for social login
    private User registerNewUser(OAuthUserInfo userInfo) {
        String email = userInfo.getEmail();
        String name = userInfo.getName();

        if (userRepository.existsByEmail(email)) {
            throw new UserHandler(ErrorStatus.USER_ALREADY_EXIST);
        }

        // closit id 임의 생성
        String clositId = generateUniqueClositId(userInfo);

        // Dummy password => 일반 로그인 사용 X
        String dummyPassword = UUID.randomUUID().toString();
        String encodedPassword = passwordEncoder.encode(dummyPassword); // null 방지용;


        User newUser = User.builder()
                .email(email)
                .name(name)
                .profileImage(profileImage)
                .password(encodedPassword) // Dummy password
                .provider(userInfo.getProvider().toString())
                .clositId(clositId)
                .role(Role.USER)
                .countReport(0)
                .build();

        return userRepository.save(newUser);
    }

    private String generateUniqueClositId(OAuthUserInfo userInfo) {
        String base = userInfo.getName().replaceAll("\\s+", "").toLowerCase();
        String suffix;
        String randomClositId;

        log.info("base : {}", base);
        do {
            suffix = UUID.randomUUID().toString().substring(0, 6);
            randomClositId = base + "_" + suffix;
        } while(userRepository.existsByClositId(randomClositId));

        log.info("randomClositId : {}", randomClositId);
        return randomClositId;
    }
}