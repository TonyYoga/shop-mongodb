package com.telran.shopmongodb.service.exceptions;

public class NotFoundServiceException extends ServiceException {
    public NotFoundServiceException(String message) {
        super(message);
    }

    public NotFoundServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
