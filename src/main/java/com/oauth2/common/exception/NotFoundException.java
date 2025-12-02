// com.oauth2.common.exception.NotFoundException
package com.oauth2.common.exception;

public class NotFoundException extends BusinessException {

    public NotFoundException(String message) {
        super(ErrorCode.RESOURCE_NOT_FOUND, message);
    }

}
