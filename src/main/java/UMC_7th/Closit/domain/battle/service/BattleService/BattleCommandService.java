package UMC_7th.Closit.domain.battle.service.BattleService;

import UMC_7th.Closit.domain.battle.dto.BattleDTO.request.ChallengeBattleRequest;
import UMC_7th.Closit.domain.battle.dto.BattleDTO.request.BattleCreateRequest;
import UMC_7th.Closit.domain.battle.dto.BattleDTO.request.ChallengeBattleDecisionRequest;
import UMC_7th.Closit.domain.battle.dto.BattleDTO.request.BattleVoteRequest;
import UMC_7th.Closit.domain.battle.entity.Battle;
import UMC_7th.Closit.domain.battle.entity.ChallengeBattle;
import UMC_7th.Closit.domain.battle.entity.Vote;

public interface BattleCommandService {

    Battle createBattle(Long userId, BattleCreateRequest request); // 배틀 생성
    ChallengeBattle challengeBattle(Long userId, Long battleId, ChallengeBattleRequest request); // 배틀 신청
    Battle acceptChallenge(Long userId, Long battleId, ChallengeBattleDecisionRequest request); // 배틀 수락
    Battle rejectChallenge(Long userId, Long battleId, ChallengeBattleDecisionRequest request); // 배틀 거절
    Vote voteBattle(Long userId, Long battleId, BattleVoteRequest request); // 배틀 투표
    void completeBattle(); // 배틀 진행 상태 갱신
    void deleteBattle(Long userId, Long battleId); // 배틀 삭제
}
