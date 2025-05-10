package UMC_7th.Closit.global.apiPayload.exception.handler;

import UMC_7th.Closit.global.apiPayload.code.BaseErrorCode;
import UMC_7th.Closit.global.apiPayload.exception.GeneralException;

public class EmailEncodingHandler extends GeneralException {

    public EmailEncodingHandler(BaseErrorCode errorCode) {
      super(errorCode);
    }
}
