package com.gzu.common.core.exception;

public class TokenException extends BusinessException {
    public TokenException(String message) {
        super(ErrorCode.UNAUTHORIZED, message);
    }
}

