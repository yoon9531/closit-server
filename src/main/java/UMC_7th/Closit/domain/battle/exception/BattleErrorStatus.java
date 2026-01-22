package UMC_7th.Closit.domain.battle.exception;

import UMC_7th.Closit.global.apiPayload.code.BaseErrorCode;
import UMC_7th.Closit.global.apiPayload.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum BattleErrorStatus implements BaseErrorCode {

    // 배틀 관련 에러
    BATTLE_NOT_CHALLENGE (HttpStatus.BAD_REQUEST, "BATTLE4001", "동일한 게시글로 배틀을 신청할 수 없습니다."),
    BATTLE_ALREADY_EXIST (HttpStatus.BAD_REQUEST, "BATTLE4002", "배틀이 이미 존재합니다."),
    POST_NOT_BATTLE (HttpStatus.BAD_REQUEST, "BATTLE4003", "해당 게시글은 배틀이 아닙니다"),
    POST_ALREADY_BATTLE (HttpStatus.BAD_REQUEST, "BATTLE4004", "해당 게시글은 이미 배틀 게시글입니다."),
    POST_IS_CHALLENGE (HttpStatus.BAD_REQUEST, "BATTLE4005", "해당 게시글은 배틀 챌린지 게시글입니다."),
    BATTLE_NOT_FOUND (HttpStatus.NOT_FOUND, "BATTLE4041", "배틀이 존재하지 않습니다."),
    POST_NOT_APPLY(HttpStatus.BAD_REQUEST, "BATTLE4007", "본인의 게시글에 배틀을 신청할 수 없습니다."),
    POST_UNAUTHORIZED_ACCESS(HttpStatus.BAD_REQUEST, "BATTLE4008", "해당 게시글은 다른 사용자의 게시글입니다."),
    BATTLE_UNAUTHORIZED_ACCESS(HttpStatus.BAD_REQUEST, "BATTLE4009", "해당 배틀 게시글은 다른 사용자의 배틀 게시글 입니다."),

    // 챌린지 배틀 관련 에러
    CHALLENGE_BATTLE_NOT_FOUND(HttpStatus.NOT_FOUND, "CHALLENGEBATTLE4041", "챌린지 배틀이 존재하지 않습니다."),

    // 투표 관련 에러
    VOTE_ALREADY_EXIST (HttpStatus.BAD_REQUEST, "VOTE4001", "이미 투표를 했습니다."),
    VOTE_EXPIRED (HttpStatus.BAD_REQUEST, "VOTE4002", "이미 종료된 투표 입니다."),

    // 배틀 좋아요 관련 에러
    BATTLE_LIKES_ALREADY_EXIST (HttpStatus.BAD_REQUEST, "BATTLELIKE4001", "이미 좋아요를 누른 배틀 입니다."),
    BATTLE_LIKES_NOT_FOUND (HttpStatus.NOT_FOUND, "BATTLELIKE4041", "배틀 좋아요가 존재하지 않습니다"),

    // 배틀 댓글 관련 에러
    BATTLE_COMMENT_NOT_FOUND (HttpStatus.NOT_FOUND, "BATTLECOMMENT4041", "배틀 댓글이 존재하지 않습니다."),
    BATTLE_COMMENT_DEPTH_EXCEEDED(HttpStatus.BAD_REQUEST, "BATTLECOMMENT4001", "배틀 대댓글은 한 단계까지만 허용됩니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;


    @Override
    public ErrorReasonDTO getReason() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build()
                ;
    }
}
