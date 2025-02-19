package UMC_7th.Closit.domain.user.service;

import UMC_7th.Closit.domain.user.dto.RegisterResponseDTO;
import UMC_7th.Closit.domain.user.dto.UserRequestDTO;
import UMC_7th.Closit.domain.user.dto.UserResponseDTO;
import UMC_7th.Closit.domain.user.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

public interface UserCommandService {

    RegisterResponseDTO registerUser (UserRequestDTO.CreateUserDTO userRequestDto);
    void deleteUser();

    UserResponseDTO.UserInfoDTO registerProfileImage (MultipartFile file);

    boolean isClositIdUnique(String clositId);

    UserResponseDTO.UserInfoDTO updateUserInfo(UserRequestDTO.UpdateUserDTO updateUserDTO);
}
