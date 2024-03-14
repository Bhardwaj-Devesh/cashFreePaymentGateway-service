package com.hiwipay.paymentgatewayservice.common.error;

public enum ErrorType {
    DETAILS_NOT_PRESENT(1, "Details parameters is not present in body"),
    REQUEST_NOT_ALLOWED(2, "Not allowed:  Invalid Request"),
    NOT_FOUND(3, "Entity Not Found"),
    REST_REQUEST_FAILED(4, "RestTemplate request failed"),
    STATUS_TRANSITION_NOT_ALLOWED(5, "this status transition is not allowed"),
    YOU_ARE_NOT_AUTHORIZED(6, "You are not allowed to perform this operation");

    private final int id;
    private final String message;

    ErrorType(int id, String message) {
        this.id = id;
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }
}