package org.example.market_backend.Exception;

import org.example.market_backend.Constants.CodeEnum;
import org.example.market_backend.Constants.ResponseCode;

public class BusinessException extends RuntimeException {
    private final int code;
    private final String message;

    public BusinessException(CodeEnum codeEnum) {
        this.code = codeEnum.getCode();
        this.message = codeEnum.getMessage();
    }

    public BusinessException(ResponseCode responseCode) {
        this.code = responseCode.getCode();
        this.message = responseCode.getMessage();
    }

    public BusinessException(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
