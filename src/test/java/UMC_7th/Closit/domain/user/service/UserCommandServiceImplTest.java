package UMC_7th.Closit.domain.user.service;

import UMC_7th.Closit.domain.user.dto.UserRequestDTO;
import UMC_7th.Closit.domain.user.entity.Role;
import UMC_7th.Closit.domain.user.entity.User;
import UMC_7th.Closit.domain.user.repository.UserRepository;
import UMC_7th.Closit.global.apiPayload.code.status.ErrorStatus;
import UMC_7th.Closit.global.apiPayload.exception.handler.UserHandler;
import UMC_7th.Closit.security.SecurityUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class UserCommandServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private SecurityUtil securityUtil;
    @InjectMocks
    private UserCommandServiceImpl userCommandService;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("비활성화 API 동작 -> isActive가 false")
    void deactivateUser_success() {
        // given
        String clositId = "testuser";

        user = User.builder()
                .id(1L)
                .clositId("testuser")
                .name("테스트")
                .email("test@test.com")
                .password("pw")
                .isWithdrawn(false)
                .build();

        when(userRepository.findByClositId(anyString())).thenReturn(Optional.of(user));
        UserRequestDTO.DeactivateUserDTO dto = new UserRequestDTO.DeactivateUserDTO(clositId);

        // when
        userCommandService.deactivateUser(dto);

        // then
        assertThat(user.getIsActive()).isFalse();
    }

    @Test
    @DisplayName("탈퇴 요청 시 isWithdrawn, withdrawalRequestedAt -> true, 현재 시간")
    void deleteUser_setsWithdrawnAndDate() {
        when(securityUtil.getCurrentUser()).thenReturn(user);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        userCommandService.deleteUser();

        assertThat(user.getIsWithdrawn()).isTrue();
        assertThat(user.getWithdrawalRequestedAt()).isNotNull();
    }

    @Test
    @DisplayName("탈퇴 취소 시 isWithdrawn=false, withdrawalRequestedAt=null로 복구.")
    void cancelWithdrawal_success() {
        user.setWithdrawn(true);
        user.setWithdrawalRequestedAt(LocalDateTime.now());
        when(securityUtil.getCurrentUser()).thenReturn(user);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        userCommandService.cancelWithdrawal();
    }
    @DisplayName("존재하지 않는 사용자를 비활성화하면 예외가 발생한다")
    void deactivateUser_userNotFound() {
        // given
        when(userRepository.findByClositId(anyString())).thenReturn(Optional.empty());
        UserRequestDTO.DeactivateUserDTO dto = new UserRequestDTO.DeactivateUserDTO("notfound");

        // when & then
        assertThatThrownBy(() -> userCommandService.deactivateUser(dto))
                .isInstanceOf(UserHandler.class);
        assertThat(user.getIsWithdrawn()).isFalse();
        assertThat(user.getWithdrawalRequestedAt()).isNull();
    }

    @Test
    // 새벽 3시마다 scheduler 실행되는데, 3시 이전에 탈퇴 요청 시 이미 7일이 지났을 경우 탈퇴 취소 요청 시
    @DisplayName("탈퇴 취소 시 7일이 지나면 예외 발생")
    void cancelWithdrawal_expired() {
        user.setWithdrawn(true);
        user.setWithdrawalRequestedAt(LocalDateTime.now().minusDays(8));
        when(securityUtil.getCurrentUser()).thenReturn(user);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> userCommandService.cancelWithdrawal())
                .isInstanceOf(UserHandler.class)
                .hasMessageContaining(ErrorStatus.WITHDRAWAL_PERIOD_EXPIRED.getMessage());
    }
}