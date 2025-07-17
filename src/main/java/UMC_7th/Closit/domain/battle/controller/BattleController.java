package UMC_7th.Closit.domain.battle.controller;

import UMC_7th.Closit.domain.battle.converter.BattleConverter;
import UMC_7th.Closit.domain.battle.dto.BattleDTO.BattleResponseDTO;
import UMC_7th.Closit.domain.battle.dto.BattleDTO.request.CreateBattleRequest;
import UMC_7th.Closit.domain.battle.dto.BattleDTO.request.VoteBattleRequest;
import UMC_7th.Closit.domain.battle.dto.BattleDTO.response.CreateBattleResponse;
import UMC_7th.Closit.domain.battle.dto.BattleDTO.response.VoteBattleResponse;
import UMC_7th.Closit.domain.battle.entity.*;
import UMC_7th.Closit.domain.battle.entity.enums.BattleSorting;
import UMC_7th.Closit.domain.battle.entity.enums.BattleStatus;
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

@Tag(name = "[배틀 게시판]", description = "배틀 게시판 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/communities/battle")
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
    public ApiResponse<CreateBattleResponse> createBattle(@RequestBody @Valid CreateBattleRequest request) {

        User user = securityUtil.getCurrentUser();
        Long userId = user.getId();

        Battle battle = battleCommandService.createBattle(userId, request);

        return ApiResponse.onSuccess(CreateBattleResponse.from(battle));
    }

    @PostMapping("/{battleId}/voting")
    @Operation(summary = "배틀 투표",
            description = """
            ## 배틀 게시글 중 한 게시글에 배틀 투표
            ### PathVariable
            battleId [배틀 ID]
            ### RequestBody
            post_id [게시글 ID]
            """)
    public ApiResponse<VoteBattleResponse> voteBattle(
            @RequestBody @Valid VoteBattleRequest request,
            @PathVariable("battleId") Long battleId
    ) {
        User user = securityUtil.getCurrentUser();
        Long userId = user.getId();

        Vote voteBattle = battleCommandService.voteBattle(userId, battleId, request);

        return ApiResponse.onSuccess(VoteBattleResponse.from(voteBattle));
    }

    @GetMapping()
    @Operation(summary = "배틀 게시글 목록 조회",
            description = """
            ## 배틀 게시글 목록 조회 - 투표 하지 않은 배틀 게시글은 0으로 보임
            ### Parameters
            page [조회할 페이지 번호] - 0부터 시작, 10개씩 보여줌 \n
            sorting [배틀 정렬 방식] - 최신순 (LATEST), 조회순 (VIEW) \n
            status [배틀 진행 상태] - 진행 중 (ACTIVE), 종료 (COMPLETED)
            """)
    public ApiResponse<BattleResponseDTO.BattlePreviewListDTO> getBattleList(@RequestParam(name = "page") Integer page,
                                                                             @RequestParam(name ="sorting") BattleSorting sorting,
                                                                             @RequestParam(name = "status") BattleStatus status) {

        Slice<Battle> battleList = battleQueryService.getBattleList(page, sorting, status);

        return ApiResponse.onSuccess(BattleConverter.battlePreviewListDTO(battleList));
    }

    @GetMapping("/{battleId}")
    @Operation(summary = "배틀 게시글 상세 조회",
            description = """
            ## 배틀 게시글 상세 조회 - 투표 하지 않은 배틀 게시글은 투표 수 = 0으로 보임
            ### PathVariable
            battleId - [조회할 배틀 ID]
            """)
    public ApiResponse<BattleResponseDTO.GetBattleDetailDTO> getBattleDetail(@PathVariable("battleId") Long battleId) {
        Battle battle = battleQueryService.getBattleDetail(battleId);

        return ApiResponse.onSuccess(BattleConverter.getBattleDetail(battle));
    }

    @GetMapping("/voted-posts")
    @Operation(summary = "내가 투표한 게시글 목록 조회",
            description = """
            ## 내가 투표한 배틀 게시글 목록 조회
            ### Parameters
            page [조회할 페이지 번호] - 0부터 시작, 10개씩 보여줌
            """)
    public ApiResponse<BattleResponseDTO.BattlePreviewListDTO> getMyVotedPostList(@RequestParam(name = "page") Integer page) {

        Slice<Battle> votedBattleList = battleQueryService.getMyVotedBattleList(page);

        return ApiResponse.onSuccess(BattleConverter.battlePreviewListDTO(votedBattleList));
    }

    @DeleteMapping("/{battleId}")
    @Operation(summary = "배틀 삭제",
            description = """
            ## 특정 배틀 게시글 삭제
            ### PathVariable
            battle_id [배틀 ID]
            """)
    public ApiResponse<String> deleteBattle(@PathVariable("battleId") Long battleId) {

        User user = securityUtil.getCurrentUser();
        Long userId = user.getId();

        battleCommandService.deleteBattle(userId, battleId);

        return ApiResponse.onSuccess("Deletion successful");
    }
}
