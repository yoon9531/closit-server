package UMC_7th.Closit.global.apiPayload.exception.handler;

import UMC_7th.Closit.global.apiPayload.code.BaseErrorCode;
import UMC_7th.Closit.global.apiPayload.exception.GeneralException;

public class JwtHandler extends GeneralException {
    public JwtHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
