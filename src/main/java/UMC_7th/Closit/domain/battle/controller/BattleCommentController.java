package UMC_7th.Closit.domain.battle.controller;

import UMC_7th.Closit.domain.battle.converter.BattleCommentConverter;
import UMC_7th.Closit.domain.battle.dto.BattleCmtDTO.BattleCommentRequestDTO;
import UMC_7th.Closit.domain.battle.dto.BattleCmtDTO.BattleCommentResponseDTO;
import UMC_7th.Closit.domain.battle.entity.BattleComment;
import UMC_7th.Closit.domain.battle.service.BattleCmtService.BattleCmtCommandService;
import UMC_7th.Closit.domain.battle.service.BattleCmtService.BattleCmtQueryService;
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
public class BattleCommentController {

    private final BattleCmtCommandService battleCmtCommandService;
    private final BattleCmtQueryService battleCmtQueryService;
    private final SecurityUtil securityUtil;

    @PostMapping("/{battle_id}/comments")
    @Operation(summary = "새로운 배틀 댓글 생성",
            description = """
            ## 배틀 게시글에 댓글 작성
            ### PathVariable
            battle_id [배틀 ID]
            ### RequestBody
            content [댓글 내용] \n
            parentCommentId [부모 댓글 ID - 대댓글일 경우에만 요청]
            """)
    public ApiResponse<BattleCommentResponseDTO.CreateBattleCommentDTO> createBattleComment(@RequestBody @Valid BattleCommentRequestDTO.CreateBattleCommentDTO request,
                                                                                            @PathVariable("battle_id") Long battleId) {

        User user = securityUtil.getCurrentUser();
        Long userId = user.getId();

        BattleComment battleComment = battleCmtCommandService.createBattleComment(userId, battleId, request);

        return ApiResponse.onSuccess(BattleCommentConverter.createBattleCommentResponseDTO(battleComment));
    }

    @GetMapping("/{battle_id}/comments")
    @Operation(summary = "배틀 댓글 조회",
            description = """
            ## 배틀 게시글 댓글 목록 조회
            ### PathVariable
            battle_id [배틀 ID]
            ### Parameters
            page [조회할 페이지 번호] - 0부터 시작, 10개씩 보여줌
            """)
    public ApiResponse<BattleCommentResponseDTO.BattleCommentPreviewListDTO> getBattleComments(@PathVariable("battle_id") Long battleId,
                                                                                               @RequestParam(name = "page") Integer page) {

        Slice<BattleComment> battleCommentList = battleCmtQueryService.getBattleCommentList(battleId, page);

        return ApiResponse.onSuccess(BattleCommentConverter.battleCommentPreviewListDTO(battleCommentList));
    }

    @DeleteMapping("/{battle_id}/comments/{battle_comment_id}")
    @Operation(summary = "배틀 댓글 삭제",
            description = """
            ## 배틀 게시글 내 특정 댓글 삭제
            ### PathVariable
            battle_id [배틀 ID] \n
            battle_comment_id [배틀 댓글 ID]
            """)
    public ApiResponse<String> deleteBattleComment(@PathVariable("battle_id") Long battleId,
                                                   @PathVariable("battle_comment_id") Long battleCommentId) {

        User user = securityUtil.getCurrentUser();
        Long userId = user.getId();

        battleCmtCommandService.deleteBattleComment(userId, battleId, battleCommentId);

        return ApiResponse.onSuccess("Deletion successful");
    }
}