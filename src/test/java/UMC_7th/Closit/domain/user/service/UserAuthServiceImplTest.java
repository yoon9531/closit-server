package UMC_7th.Closit.domain.user.service;

import UMC_7th.Closit.domain.user.dto.LoginRequestDTO;
import UMC_7th.Closit.domain.user.entity.Role;
import UMC_7th.Closit.domain.user.entity.User;
import UMC_7th.Closit.domain.user.repository.UserRepository;
import UMC_7th.Closit.global.apiPayload.code.status.ErrorStatus;
import UMC_7th.Closit.global.apiPayload.exception.GeneralException;
import UMC_7th.Closit.global.apiPayload.exception.handler.UserHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class UserAuthServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserAuthServiceImpl userAuthService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
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