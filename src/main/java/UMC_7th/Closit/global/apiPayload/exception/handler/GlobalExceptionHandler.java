package UMC_7th.Closit.global.apiPayload.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<String> handleMaxSizeException (MaxUploadSizeExceededException ex) {
        log.error("❌ Max upload size exceeded: {}", ex.getMessage(), ex);
        return ResponseEntity
                .status(HttpStatus.PAYLOAD_TOO_LARGE) // 413 상태 코드 사용
                .body("파일 크기가 제한을 초과했습니다. 최대 업로드 크기를 확인하세요.");
    }

    // MultipartException 예외 처리 (Tomcat에서 발생하는 예외 방지)
    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<String> handleMultipartException(MultipartException ex) {
        log.error("파일 업로드 처리 중 예외 발생: {}", ex.getMessage(), ex);
        return ResponseEntity
                .status(HttpStatus.PAYLOAD_TOO_LARGE)
                .body("파일 크기가 제한을 초과했습니다. 업로드 크기를 확인하세요.");
    }
}
