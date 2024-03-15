package com.hiwipay.paymentgatewayservice.cashFreePaymentGateway.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter @Setter
public class CashFreeCreateOrderRequestDto {
    
    private String hiwiId;
    private String payerUserHashId;
}
