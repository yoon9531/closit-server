package UMC_7th.Closit.domain.battle.controller;

import UMC_7th.Closit.domain.battle.converter.BattleConverter;
import UMC_7th.Closit.domain.battle.dto.BattleDTO.BattleRequestDTO;
import UMC_7th.Closit.domain.battle.dto.BattleDTO.BattleResponseDTO;
import UMC_7th.Closit.domain.battle.entity.Battle;
import UMC_7th.Closit.domain.battle.entity.ChallengeBattle;
import UMC_7th.Closit.domain.battle.entity.Vote;
import UMC_7th.Closit.domain.battle.service.BattleService.BattleCommandService;
import UMC_7th.Closit.domain.battle.service.BattleService.BattleQueryService;
import UMC_7th.Closit.domain.user.entity.User;
import UMC_7th.Closit.global.apiPayload.ApiResponse;
import UMC_7th.Closit.security.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/communities/battle")
public class BattleController {

    private final BattleCommandService battleCommandService;
    private final BattleQueryService battleQueryService;
    private final SecurityUtil securityUtil;

    @PostMapping("/upload")
    @Operation(summary = "새로운 배틀 생성",
            description = """
            ## 새로운 배틀 게시글 업로드
            ### RequestBody
            post_id [게시글 ID] \n
            title [배틀 게시글 제목]
            """)
    public ApiResponse<BattleResponseDTO.CreateBattleResultDTO> createBattle(@RequestBody @Valid BattleRequestDTO.CreateBattleDTO request) {

        User user = securityUtil.getCurrentUser();
        Long userId = user.getId();

        Battle battle = battleCommandService.createBattle(userId, request);

        return ApiResponse.onSuccess(BattleConverter.createBattleResultDTO(battle));
    }

    @PostMapping("/challenge/upload/{battle_id}")
    @Operation(summary = "배틀 신청",
            description = """
            ## 배틀 챌린지 게시글에 배틀 도전
            ### PathVariable
            battle_id [배틀 ID]
            ### RequestBody
            post_id [게시글 ID]
            """)
    public ApiResponse<BattleResponseDTO.ChallengeBattleResultDTO> challengeBattle(@RequestBody @Valid BattleRequestDTO.ChallengeBattleDTO request,
                                                                                   @PathVariable("battle_id") Long battleId) {

        User user = securityUtil.getCurrentUser();
        Long userId = user.getId();

        ChallengeBattle challengeBattle = battleCommandService.challengeBattle(userId, battleId, request);

        return ApiResponse.onSuccess(BattleConverter.challengeBattleResultDTO(challengeBattle));
    }

    @PatchMapping("/{battleId}/challenge/accept")
    @Operation(summary = "배틀 신청 수락",
            description = """
            ## 배틀 신청 수락
            ### PathVariable
            battleId [배틀 ID]
            ### RequestBody
            challengeBattleId [챌린지 배틀 ID]
            """)
    public ApiResponse<BattleResponseDTO.ChallengeDecisionDTO> acceptChallenge(@RequestBody @Valid BattleRequestDTO.ChallengeDecisionDTO request,
                                                                               @PathVariable("battleId") Long battleId) {
        User user = securityUtil.getCurrentUser();
        Long userId = user.getId();

        Battle battle = battleCommandService.acceptChallenge(userId, battleId, request);

        return ApiResponse.onSuccess(BattleConverter.acceptChallengeDTO(battle));
    }

    @PostMapping("/{battle_id}/voting")
    @Operation(summary = "배틀 투표",
            description = """
            ## 배틀 게시글 중 한 게시글에 배틀 투표
            ### PathVariable
            battle_id [배틀 ID]
            ### RequestBody
            post_id [게시글 ID]
            """)
    public ApiResponse<BattleResponseDTO.VoteBattleResultDTO> voteBattle(@RequestBody @Valid BattleRequestDTO.VoteBattleDTO request,
                                                                         @PathVariable("battle_id") Long battleId) {

        User user = securityUtil.getCurrentUser();
        Long userId = user.getId();

        Vote voteBattle = battleCommandService.voteBattle(userId, battleId, request);

        return ApiResponse.onSuccess(BattleConverter.voteBattleResultDTO(voteBattle));
    }

    @GetMapping()
    @Operation(summary = "배틀 게시글 목록 조회",
            description = """
            ## 배틀 게시글 목록 조회 - 투표 하지 않은 배틀 게시글은 0으로 보임
            ### Parameters
            page [조회할 페이지 번호] - 0부터 시작, 10개씩 보여줌
            """)
    public ApiResponse<BattleResponseDTO.BattlePreviewListDTO> getBattleList(@RequestParam(name = "page") Integer page) {

        Slice<Battle> battleList = battleQueryService.getBattleList(page);

        return ApiResponse.onSuccess(BattleConverter.battlePreviewListDTO(battleList));
    }

    @GetMapping("/challenge")
    @Operation(summary = "배틀 챌린지 게시글 목록 조회",
            description = """
            ## 배틀 챌린지 게시글 목록 조회 - 첫 번째 게시글만 존재할 경우
            ### Parameters
            page [조회할 페이지 번호] - 0부터 시작, 10개씩 보여줌
            """)
    public ApiResponse<BattleResponseDTO.ChallengeBattlePreviewListDTO> getChallengeBattleList(@RequestParam(name = "page") Integer page) {

        Slice<Battle> challengeBattleList = battleQueryService.getChallengeBattleList(page);

        return ApiResponse.onSuccess(BattleConverter.challengeBattlePreviewListDTO(challengeBattleList));
    }

    @DeleteMapping("/{battle_id}")
    @Operation(summary = "배틀 삭제",
            description = """
            ## 특정 배틀 게시글 삭제
            ### PathVariable
            battle_id [배틀 ID]
            """)
    public ApiResponse<String> deleteBattle(@PathVariable("battle_id") Long battleId) {

        User user = securityUtil.getCurrentUser();
        Long userId = user.getId();

        battleCommandService.deleteBattle(userId, battleId);

        return ApiResponse.onSuccess("Deletion successful");
    }
}
