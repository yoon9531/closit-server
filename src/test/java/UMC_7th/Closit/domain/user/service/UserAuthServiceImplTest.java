package UMC_7th.Closit.domain.user.service;

import UMC_7th.Closit.domain.user.dto.LoginRequestDTO;
import UMC_7th.Closit.domain.user.dto.UserRequestDTO;
import UMC_7th.Closit.domain.user.entity.Role;
import UMC_7th.Closit.domain.user.entity.User;
import UMC_7th.Closit.domain.user.repository.UserRepository;
import UMC_7th.Closit.global.apiPayload.code.status.ErrorStatus;
import UMC_7th.Closit.global.apiPayload.exception.GeneralException;
import UMC_7th.Closit.global.apiPayload.exception.handler.UserHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserAuthServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserUtil userUtil;
    @InjectMocks
    private UserAuthServiceImpl userAuthService;

    @InjectMocks
    private UserCommandServiceImpl userCommandService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        UserRequestDTO.CreateUserDTO dto = UserRequestDTO.CreateUserDTO.builder()
                .clositId("testuser")
                .password("password")
                .email("hello@gmail.com")
                .build();
        userCommandService.registerUser(dto);
    }

    @AfterEach
    void afterEach () {
        userRepository.deleteByClositId("testuser");
    }
    @Test
    @DisplayName("비활성화된 사용자는 로그인할 수 없다")
    void inactivateUser_cannot_login() {
        // given
        String clositId = "testuser";
        String password = "password";

        User inactiveUser = User.builder()
                .clositId(clositId)
                .password(password)
                .role(Role.USER)
                .isActive(false)
                .build();

        when(userRepository.findByClositId(anyString())).thenReturn(Optional.of(inactiveUser));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        LoginRequestDTO dto = new LoginRequestDTO(clositId, password);

        // when & then
        assertThatThrownBy(() -> userAuthService.login(dto))
                .isInstanceOf(GeneralException.class)
                .hasMessageContaining(ErrorStatus.USER_NOT_ACTIVE.getMessage());
    }
}