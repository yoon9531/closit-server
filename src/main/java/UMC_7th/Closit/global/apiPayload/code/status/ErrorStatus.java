package UMC_7th.Closit.global.apiPayload.code.status;

import UMC_7th.Closit.global.apiPayload.code.BaseErrorCode;
import UMC_7th.Closit.global.apiPayload.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {

    // 가장 일반적인 응답
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST,"COMMON400","잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"COMMON401","인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),
    _NOT_FOUND(HttpStatus.NOT_FOUND, "COMMON404", "찾을 수 없습니다."),

    // 사용자 관련 에러
    PASSWORD_NOT_CORRESPOND (HttpStatus.UNAUTHORIZED, "LOGIN4001", "비밀번호가 일치하지 않습니다."), // 인증 실패
    USER_NOT_LOGGED_IN(HttpStatus.UNAUTHORIZED, "USER4001", "로그인이 필요합니다. 다시 로그인해주세요."),
    USER_ALREADY_EXIST (HttpStatus.CONFLICT, "REGISTER4002", "이미 존재하는 사용자입니다."), // 리소스 충돌 (회원가입 시)
    USER_NOT_AUTHORIZED (HttpStatus.FORBIDDEN, "USER4003", "사용자 권한이 없습니다."), // 권한 부족
    USER_NOT_MATCH (HttpStatus.FORBIDDEN, "USER4004", "사용자가 일치하지 않습니다."), // 권한 부족
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "USER4005","이미 존재하는 이메일입니다"), // 리소스 충돌
    USER_NOT_FOUND (HttpStatus.NOT_FOUND, "USER4006", "사용자가 존재하지 않습니다."), // 존재하지 않는 사용자
    USER_NOT_BLOCKED(HttpStatus.NOT_FOUND, "USER4007", "사용자가 차단되지 않았습니다."), // 차단되지 않은 사용자
    CLOSIT_ID_ALREADY_EXISTS(HttpStatus.CONFLICT, "USER4007", "이미 존재하는 ClositId입니다."), // 리소스 충돌
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "USER4008", "비밀번호가 유효하지 않습니다."), // 비밀번호 유효성 검사 실패
    NO_CHANGE_DETECTED(HttpStatus.BAD_REQUEST, "USER4009", "변경된 내용이 없습니다."), // 변경된 내용이 없음
    USER_ALREADY_BLOCKED(HttpStatus.CONFLICT, "USER4010", "이미 차단한 사용자입니다."), // 이미 차단한 사용자
    PROFILE_IMAGE_EMPTY(HttpStatus.BAD_REQUEST, "USER4010", "프로필 이미지가 비어 있습니다."),
    EMAIL_NOT_VERIFIED(HttpStatus.BAD_REQUEST, "USER4011", "이메일 인증이 완료되지 않았습니다."),

    // 토큰 관련 에러
    EXPIRED_TOKEN(HttpStatus.BAD_REQUEST, "TOKEN4001", "토큰이 만료되었습니다."),
    INVALID_TOKEN(HttpStatus.BAD_REQUEST, "TOKEN4002", "유효하지 않은 토큰입니다."),
    UNSUPPORTED_TOKEN(HttpStatus.BAD_REQUEST, "TOKEN4003", "지원하지 않는 토큰입니다."),
    EMPTY_TOKEN(HttpStatus.BAD_REQUEST, "TOKEN4004", "토큰이 비어있습니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "TOKEN4005", "유효하지 않은 리프레시 토큰입니다."),

    // 이메일 인증 관련 에러
    EMAIL_TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "EMAIL4041", "해당 이메일 인증 토큰이 존재하지 않습니다."),
    EMAIL_TOKEN_ALREADY_VERIFIED(HttpStatus.BAD_REQUEST, "EMAIL4001", "이미 인증된 토큰입니다."),
    EMAIL_TOKEN_ALREADY_USED(HttpStatus.BAD_REQUEST, "EMAIL4002", "이미 사용된 토큰입니다."),
    EMAIL_TOKEN_EXPIRED(HttpStatus.BAD_REQUEST, "EMAIL4003", "토큰이 만료되었습니다."),
    EMAIL_TOKEN_NOT_AVAILABLE(HttpStatus.NOT_FOUND, "EMAIL4042", "인증된 토큰이 존재하지 않습니다."),
    EMAIL_TOKEN_INVALID_FOR_USE(HttpStatus.BAD_REQUEST, "EMAIL4004", "유효한 인증 토큰이 아닙니다."),
    EMAIL_SEND_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "EMAIL5001", "이메일 전송에 실패했습니다."),
    EMAIL_SENDER_ENCODING_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "EMAIL5002", "이메일 발신자 이름 인코딩에 실패했습니다."),
    EMAIL_TEMPLATE_LOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "EMAIL5003", "이메일 템플릿을 불러오는 데 실패했습니다."),

    // 소셜 로그인 관련 에러
    NOT_SUPPORTED_SOCIAL_LOGIN(HttpStatus.BAD_REQUEST, "SOCIAL4001", "지원하지 않는 소셜 로그인입니다."),

    // 게시글 관련 에러
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "POST4041", "게시글이 존재하지 않습니다."),
    INVALID_HASHTAG_LENGTH(HttpStatus.BAD_REQUEST, "POST4001", "해시태그는 20자 이내여야 합니다."),
    INVALID_ITEMTAG_LENGTH(HttpStatus.BAD_REQUEST, "POST4002", "아이템 태그는 20자 이내여야 합니다."),

    // 북마크 관련 에러
    BOOKMARK_NOT_FOUND(HttpStatus.NOT_FOUND,"BOOKMARK4041","북마크가 존재하지 않습니다."),

    // 좋아요 관련 에러
    LIKES_ALREADY_EXIST (HttpStatus.BAD_REQUEST, "LIKE4001", "이미 좋아요를 누른 게시글 입니다."),
    LIKES_NOT_FOUND(HttpStatus.NOT_FOUND, "LIKE4041", "좋아요가 존재하지 않습니다."),

    // 댓글 관련 에러
    COMMENT_NOT_FOUND (HttpStatus.NOT_FOUND, "COMMENT4041", "댓글이 존재하지 않습니다."),
    COMMENT_NOT_MINE (HttpStatus.BAD_REQUEST, "COMMENT4002", "해당 댓글은 다른 사용자의 댓글입니다"),

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

    // 하이라이트 관련 에러
    HIGHLIGHT_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "HIGHLIGHT4001", "이미 존재하는 하이라이트 입니다."),
    HIGHLIGHT_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "HIGHLIGHT4002", "하이라이트는 최대 5개까지만 등록할 수 있습니다."),
    HIGHLIGHT_NOT_FOUND(HttpStatus.NOT_FOUND, "HIGHLIGHT4041", "하이라이트가 존재하지 않습니다."),

    // 팔로우 관련 에러
    FOLLOW_SELF_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "FOLLOW4001", "자기 자신을 팔로우할 수 없습니다."),
    FOLLOW_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "FOLLOW4002", "이미 팔로우한 사용자입니다."),
    FOLLOW_NOT_FOUND(HttpStatus.NOT_FOUND, "FOLLOW4041", "팔로우가 존재하지 않습니다."),

    // 미션 관련 에러
    MISSION_NOT_FOUND(HttpStatus.NOT_FOUND, "MISSION4041", "미션이 존재하지 않습니다."),

    // 오늘의 옷장 관련 에러
    TODAY_CLOSET_NOT_FOUND(HttpStatus.NOT_FOUND, "TODAYCLOSET4001", "오늘의 옷장이 존재하지 않습니다."),
    DUPLICATE_TODAY_CLOSET(HttpStatus.CONFLICT, "TODAYCLOSET4002", "이미 오늘의 옷장에 등록된 게시글입니다."),

    // 해시태그 관련 관련 에러
    HASHTAG_NOT_FOUND(HttpStatus.NOT_FOUND, "HASHTAG4041", "해시태그가 존재하지 않습니다."),

    // 알림 관련 에러
    SSE_CONNECT_FAILED (HttpStatus.INTERNAL_SERVER_ERROR, "SSE5001", "SSE 연결에 실패했습니다"),
    NOTIFICATION_PUSH_FAILED (HttpStatus.BAD_REQUEST, "NOTIFICATION4001", "알림 전송에 실패했습니다."),
    NOTIFICATION_NOT_FOUND (HttpStatus.NOT_FOUND, "NOTIFICATION4041", "알림을 찾을 수 없습니다."),

    // 파일 업로드 관련 에러
    MAX_UPLOAD_SIZE_EXCEEDED(HttpStatus.BAD_REQUEST, "UPLOAD4001", "파일 업로드 크기 제한을 초과했습니다."),

    // S3 관련 에러
    INVALID_S3_FILE_URL(HttpStatus.BAD_REQUEST, "S34001", "유효하지 않은 S3 파일 URL입니다."),
    IMAGE_NOT_DELETE(HttpStatus.INTERNAL_SERVER_ERROR, "S35001", "파일 삭제에 실패했습니다.");

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
