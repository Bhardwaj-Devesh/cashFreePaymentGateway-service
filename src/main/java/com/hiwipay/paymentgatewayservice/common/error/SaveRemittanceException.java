package com.hiwipay.paymentgatewayservice.common.error;

public class SaveRemittanceException extends RuntimeException {
    public SaveRemittanceException() {
    }

    public SaveRemittanceException(String message) {
        super(message);
    }
}
