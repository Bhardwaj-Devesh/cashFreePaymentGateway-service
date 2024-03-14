package com.hiwipay.paymentgatewayservice.common.types;

public enum TxnStatusType {
    PENDING,
    CANCELLED,
    COMPLETED,
    EXPIRED,
    SUCCESSFUL,
    FAILED_BY_BANK,
    FAILED_BY_HIWI,
    REFUNDED,
    FUNDS_RECEIVED
}
