package com.hiwipay.paymentgatewayservice.cashFreePaymentGateway.dto;

import java.util.Map;

import com.hiwipay.paymentgatewayservice.common.dto.LedgerDto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder( toBuilder = true)  
public class CreateOrderEntryRequest {
    private LedgerDto ledger;
    private Map<String,String> customerDetails;
    private Map<String,String> orderMeta;
    private Map<String,String> orderTags;
}
