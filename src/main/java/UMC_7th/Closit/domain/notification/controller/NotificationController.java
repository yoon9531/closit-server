package UMC_7th.Closit.domain.notification.controller;

import UMC_7th.Closit.domain.notification.converter.NotificationConverter;
import UMC_7th.Closit.domain.notification.dto.NotificationResponseDTO;
import UMC_7th.Closit.domain.notification.entity.Notification;
import UMC_7th.Closit.domain.notification.service.NotiCommandService;
import UMC_7th.Closit.domain.notification.service.NotiQueryService;
import UMC_7th.Closit.domain.user.entity.User;
import UMC_7th.Closit.global.apiPayload.ApiResponse;
import UMC_7th.Closit.security.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/notifications")
public class NotificationController {

    private final NotiCommandService notiCommandService;
    private final NotiQueryService notiQueryService;
    private final SecurityUtil securityUtil;

    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Operation(summary = "SSE 연결",
            description = """
            ### Postman으로만 테스트 가능
            ### Headers -> Authorization -> Bearer + accessToken 넣어서 진행
            """)
    public SseEmitter createNotification(@RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId) {

        User user = securityUtil.getCurrentUser();
        Long userId = user.getId();

        SseEmitter sseEmitter = notiCommandService.subscribe(userId,lastEventId);

        return sseEmitter;
    }

    @PatchMapping("/{notification_id}")
    @Operation(summary = "알림 단건 조회",
            description = """
            ## 알림 단건 조회 후 읽음 처리
            notification_id [알림 ID]
            """)
    public ApiResponse<NotificationResponseDTO.NotiPreviewDTO> getNotification(@PathVariable Long notification_id) {

        User user = securityUtil.getCurrentUser();
        Long userId = user.getId();

        Notification notification = notiQueryService.readNotification(userId, notification_id);

        return ApiResponse.onSuccess(NotificationConverter.getNotiPreviewDTO(notification));
    }

    @PatchMapping()
    @Operation(summary = "알림 목록 조회",
            description = """
             ## 특정 사용자의 알림 목록 조회 후 읽음 처리 (최신 순 정렬)
             ### Parameters
             page [조회할 페이지 번호] - 0부터 시작, 10개씩 보여줌
             """)
    public ApiResponse<NotificationResponseDTO.NotiPreviewListDTO> getNotificationList(@RequestParam(name = "page") Integer page) {

        User user = securityUtil.getCurrentUser();
        Long userId = user.getId();

        Slice<Notification> notificationList = notiQueryService.getNotificationList(userId, page);

        return ApiResponse.onSuccess(NotificationConverter.getNotiPreviewListDTO(notificationList));
    }

    @DeleteMapping("/{notification_id}")
    @Operation(summary = "알림 삭제",
            description = """
             ## 특정 알림 삭제
             notification_id [알림 ID]
             """)
    public ApiResponse<String> deleteNotification(@PathVariable Long notification_id) {

        User user = securityUtil.getCurrentUser();
        Long userId = user.getId();

        notiCommandService.deleteNotification(userId, notification_id);

        return ApiResponse.onSuccess("Deletion successful");
    }
}