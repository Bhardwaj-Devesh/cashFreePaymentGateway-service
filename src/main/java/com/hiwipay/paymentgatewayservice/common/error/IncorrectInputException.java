package com.hiwipay.paymentgatewayservice.common.error;

public class IncorrectInputException extends RuntimeException {
    public IncorrectInputException() {
    }

    public IncorrectInputException(String message) {
        super(message);
    }
}
