package UMC_7th.Closit.global.apiPayload.exception.handler;

import UMC_7th.Closit.global.apiPayload.code.BaseErrorCode;
import UMC_7th.Closit.global.apiPayload.exception.GeneralException;

public class EmailTokenHandler extends GeneralException {

    public EmailTokenHandler(BaseErrorCode errorCode) {
      super(errorCode);
    }
}
