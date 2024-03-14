package com.hiwipay.paymentgatewayservice.common.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class GenericResponse {
    private String status;
    private String message;
}

