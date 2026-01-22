package UMC_7th.Closit.global.apiPayload.exception.handler;

import UMC_7th.Closit.global.apiPayload.code.BaseErrorCode;
import UMC_7th.Closit.global.apiPayload.exception.GeneralException;

public class HighlightHandler extends GeneralException {

    public HighlightHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
