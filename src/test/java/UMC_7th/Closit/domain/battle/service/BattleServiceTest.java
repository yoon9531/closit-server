package UMC_7th.Closit.domain.battle.service;

import UMC_7th.Closit.domain.battle.dto.BattleDTO.BattleRequestDTO;
import UMC_7th.Closit.domain.battle.entity.Battle;
import UMC_7th.Closit.domain.battle.repository.BattleRepository;
import UMC_7th.Closit.domain.battle.service.BattleService.BattleCommandServiceImpl;
import UMC_7th.Closit.domain.post.entity.Post;
import UMC_7th.Closit.domain.post.repository.PostRepository;
import UMC_7th.Closit.domain.user.entity.User;
import UMC_7th.Closit.security.SecurityUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class BattleServiceTest {

    private User mockUser;
    private Post mockPost1;

    @Mock
    private SecurityUtil securityUtil;

    @Mock
    private PostRepository postRepository;

    @Mock
    private BattleRepository battleRepository;

    @InjectMocks
    private BattleCommandServiceImpl battleCommandService;

    @BeforeEach
    void setUp() {
        mockUser = User.builder()
                .id(1L)
                .clositId("test1")
                .name("테스트1")
                .email("test1@test.com")
                .password("123456789")
                .isWithdrawn(false)
                .build();

        mockPost1 = Post.builder()
                .id(1L)
                .user(mockUser)
                .build();
    }

    @Test
    @DisplayName("사용자가 배틀을 생성한다.")
    void createBattle() {
        // given
        BattleRequestDTO.CreateBattleDTO request = BattleRequestDTO.CreateBattleDTO.builder()
                .postId(1L)
                .title("battle test")
                .description("battle test description")
                .build();

        Battle savedBattle = Battle.builder()
                .id(1L)
                .post1(mockPost1)
                .build();

        when(postRepository.findById(1L)).thenReturn(Optional.ofNullable(mockPost1));
        when(battleRepository.save(any(Battle.class))).thenReturn(savedBattle);

        // when
        Battle result = battleCommandService.createBattle(mockUser.getId(), request);

        // then
        assertAll(
                () -> assertThat(result).isNotNull(),
                () -> assertThat(result.getId()).isEqualTo(1L),
                () -> assertThat(result.getPost1().getId()).isEqualTo(mockPost1.getId()),
                () -> assertThat(result.getPost1().getUser().getId()).isEqualTo(mockUser.getId())
        );
    }
}
