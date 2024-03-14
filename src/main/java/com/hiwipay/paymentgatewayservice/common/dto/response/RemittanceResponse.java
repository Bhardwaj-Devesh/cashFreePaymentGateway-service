package com.hiwipay.paymentgatewayservice.common.dto.response;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class RemittanceResponse {
    private final HttpStatus httpStatus;
    private final String message;
    private final Object details;

    public RemittanceResponse(HttpStatus httpStatus, String message, Object details) {
        this.httpStatus = httpStatus;
        this.message = message;
        this.details = details;
    }
}
