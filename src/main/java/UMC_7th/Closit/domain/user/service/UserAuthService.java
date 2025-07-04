package UMC_7th.Closit.domain.user.service;

import UMC_7th.Closit.domain.user.dto.JwtResponse;
import UMC_7th.Closit.domain.user.dto.LoginRequestDTO;
import UMC_7th.Closit.domain.user.dto.OAuthLoginRequestDTO;
import UMC_7th.Closit.domain.user.dto.UserResponseDTO;
import UMC_7th.Closit.domain.user.entity.Role;
import UMC_7th.Closit.global.common.SocialLoginType;

public interface UserAuthService {

    JwtResponse login(LoginRequestDTO loginRequestDto);

    UserResponseDTO.UserInfoDTO updateUserRole(Long userId, Role role);

    JwtResponse refresh(String refreshToken);

    String findClositIdByEmail(String email);

    void resetPassword(String email, String newPassword);
  
    JwtResponse socialLogin (SocialLoginType socialLoginType, OAuthLoginRequestDTO oauthLoginRequestDTO);

    void logout (String accessToken);
}
