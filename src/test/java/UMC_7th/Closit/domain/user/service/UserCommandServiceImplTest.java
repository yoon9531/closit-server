package UMC_7th.Closit.domain.user.service;

import UMC_7th.Closit.domain.user.dto.UserRequestDTO;
import UMC_7th.Closit.domain.user.entity.Role;
import UMC_7th.Closit.domain.user.entity.User;
import UMC_7th.Closit.domain.user.repository.UserRepository;
import UMC_7th.Closit.global.apiPayload.exception.handler.UserHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class UserCommandServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserCommandServiceImpl userCommandService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("비활성화 API 동작 -> isActive가 false")
    void deactivateUser_success() {
        // given
        String clositId = "testuser";
        User user = User.builder()
                .clositId(clositId)
                .role(Role.USER)
                .isActive(true)
                .build();

        when(userRepository.findByClositId(anyString())).thenReturn(Optional.of(user));
        UserRequestDTO.DeactivateUserDTO dto = new UserRequestDTO.DeactivateUserDTO(clositId);

        // when
        userCommandService.deactivateUser(dto);

        // then
        assertThat(user.getIsActive()).isFalse();
    }

    @Test
    @DisplayName("존재하지 않는 사용자를 비활성화하면 예외가 발생한다")
    void deactivateUser_userNotFound() {
        // given
        when(userRepository.findByClositId(anyString())).thenReturn(Optional.empty());
        UserRequestDTO.DeactivateUserDTO dto = new UserRequestDTO.DeactivateUserDTO("notfound");

        // when & then
        assertThatThrownBy(() -> userCommandService.deactivateUser(dto))
                .isInstanceOf(UserHandler.class);
    }
}