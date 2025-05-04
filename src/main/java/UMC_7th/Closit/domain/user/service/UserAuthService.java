package UMC_7th.Closit.domain.user.service;

import UMC_7th.Closit.domain.user.dto.JwtResponse;
import UMC_7th.Closit.domain.user.dto.LoginRequestDTO;
import UMC_7th.Closit.domain.user.dto.UserResponseDTO;
import UMC_7th.Closit.domain.user.entity.Role;

public interface UserAuthService {

    JwtResponse login(LoginRequestDTO loginRequestDto);

    UserResponseDTO.UserInfoDTO updateUserRole(Long userId, Role role);

    JwtResponse refresh(String refreshToken);

    String findClositIdByEmail(String email);
}
