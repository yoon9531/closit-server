package UMC_7th.Closit.global.apiPayload.exception;

import UMC_7th.Closit.global.apiPayload.code.BaseErrorCode;
import UMC_7th.Closit.global.apiPayload.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GeneralException extends RuntimeException {

    private BaseErrorCode code;

    public GeneralException(BaseErrorCode code) {
        super(code.getReason().getMessage());
        this.code = code;
    }

    public ErrorReasonDTO getErrorReason() {
        return this.code.getReason();
    }

    public ErrorReasonDTO getErrorReasonHttpStatus(){
        return this.code.getReasonHttpStatus();
    }
}
