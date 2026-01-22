package UMC_7th.Closit.domain.battle.controller;

import UMC_7th.Closit.domain.battle.converter.BattleMapper;
import UMC_7th.Closit.domain.battle.dto.BattleDTO.BattleResponseDTO;
import UMC_7th.Closit.domain.battle.dto.BattleDTO.request.ChallengeBattleRequest;
import UMC_7th.Closit.domain.battle.dto.BattleDTO.request.ChallengeBattleDecisionRequest;
import UMC_7th.Closit.domain.battle.dto.BattleDTO.response.ChallengeBattleResponse;
import UMC_7th.Closit.domain.battle.dto.BattleDTO.response.ChallengeBattleDeicisionResponse;
import UMC_7th.Closit.domain.battle.dto.BattleDTO.response.ChallengeBattleDetailResponse;
import UMC_7th.Closit.domain.battle.entity.Battle;
import UMC_7th.Closit.domain.battle.entity.ChallengeBattle;
import UMC_7th.Closit.domain.battle.service.BattleService.BattleCommandService;
import UMC_7th.Closit.domain.battle.service.BattleService.BattleQueryService;
import UMC_7th.Closit.domain.user.entity.User;
import UMC_7th.Closit.global.apiPayload.ApiResponse;
import UMC_7th.Closit.security.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.*;

@Tag(name = "[챌린지 배틀]", description = "챌린지 배틀 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/communities/battle")
public class ChallengeBattleController {

    private final BattleCommandService battleCommandService;
    private final BattleQueryService battleQueryService;
    private final SecurityUtil securityUtil;

    @PostMapping("/{battleId}/challenge/upload")
    @Operation(summary = "배틀 신청",
            description = """
            ## 챌린지 배틀 게시글에 배틀 도전
            ### PathVariable
            battleId [배틀 ID]
            ### RequestBody
            post_id [게시글 ID]
            """)
    public ApiResponse<ChallengeBattleResponse> challengeBattle(
            @RequestBody @Valid ChallengeBattleRequest request,
            @PathVariable("battleId") Long battleId
    ) {
        User user = securityUtil.getCurrentUser();
        Long userId = user.getId();

        ChallengeBattle challengeBattle = battleCommandService.challengeBattle(userId, battleId, request);

        return ApiResponse.onSuccess(ChallengeBattleResponse.from(challengeBattle));
    }

    @GetMapping("/{battleId}/challenge/{challengeBattleId}")
    @Operation(summary = "챌린지 배틀 미리보기")
    public ApiResponse<ChallengeBattleDetailResponse> getChallengeBattle(@PathVariable("battleId") Long battleId,
                                                                         @PathVariable("challengeBattleId") Long challengeBattleId) {
        ChallengeBattle challengeBattle = battleQueryService.getChallengeBattle(battleId, challengeBattleId);

        return ApiResponse.onSuccess(ChallengeBattleDetailResponse.from(challengeBattle));
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
    public ApiResponse<ChallengeBattleDeicisionResponse> acceptChallenge(
            @RequestBody @Valid ChallengeBattleDecisionRequest request,
            @PathVariable("battleId") Long battleId
    ) {
        User user = securityUtil.getCurrentUser();
        Long userId = user.getId();

        Battle battle = battleCommandService.acceptChallenge(userId, battleId, request);

        return ApiResponse.onSuccess(ChallengeBattleDeicisionResponse.from(battle));
    }

    @PatchMapping("/{battleId}/challenge/reject")
    @Operation(summary = "배틀 신청 거절",
            description = """
            ## 배틀 신청 거절
            ### PathVariable
            battleId [배틀 ID[
            ### RequestBody
            challengeBattleId [챌린지 배틀 ID]
            """)
    public ApiResponse<ChallengeBattleDeicisionResponse> rejectChallenge(
            @RequestBody @Valid ChallengeBattleDecisionRequest request,
            @PathVariable("battleId") Long battleId
    ) {
        User user = securityUtil.getCurrentUser();
        Long userId = user.getId();

        Battle battle = battleCommandService.rejectChallenge(userId, battleId, request);

        return ApiResponse.onSuccess(ChallengeBattleDeicisionResponse.from(battle));
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

        return ApiResponse.onSuccess(BattleMapper.challengeBattlePreviewListDTO(challengeBattleList));
    }
}
