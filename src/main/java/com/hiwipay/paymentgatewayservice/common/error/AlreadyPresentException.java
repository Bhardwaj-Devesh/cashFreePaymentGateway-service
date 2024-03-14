package com.hiwipay.paymentgatewayservice.common.error;

public class AlreadyPresentException extends RuntimeException {
    public AlreadyPresentException() {
    }

    public AlreadyPresentException(String message) {
        super(message);
    }
}
