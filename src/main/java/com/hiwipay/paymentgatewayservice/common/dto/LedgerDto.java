package com.hiwipay.paymentgatewayservice.common.dto;

import com.hiwipay.paymentgatewayservice.common.Auditable;
import com.hiwipay.paymentgatewayservice.common.types.DrCrType;
import com.hiwipay.paymentgatewayservice.common.types.NeoBankType;
import com.hiwipay.paymentgatewayservice.common.types.PaymentGateway;
import com.hiwipay.paymentgatewayservice.common.types.PgMode;
import com.hiwipay.paymentgatewayservice.common.types.TxnChannelType;
import com.hiwipay.paymentgatewayservice.common.types.TxnStatusType;
import com.hiwipay.paymentgatewayservice.common.types.TxnSubType;
import com.hiwipay.paymentgatewayservice.common.types.TxnType;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class LedgerDto extends Auditable {
    private String userHashId;
    private String hiwiId;
    private String hiwiSubId;
    private String hiwiRrn;
    private float amount;
    private TxnType type;
    private TxnSubType subType;
    private float tcs;
    private float lrsLimit;
    private float bankFees;
    private DrCrType drCr;
    private String bankRrn;
    private String accountId;
    private float balance;
    private boolean batch;
    private boolean online;
    private boolean card;
    private String cardHashId;
    private PgMode pgMode;
    private TxnStatusType status;
    private TxnChannelType txnChannel;
    private String countryCodeSource;
    private String payerType;
    private String countryCodeDestination;
    private String currencyCodeSource;
    private String currencyCodeDestination;
    private NeoBankType neobankId;
    private boolean expiredNoFundsReceived;
    private Date expiringBy;
    private PaymentGateway paymentGateway;
    private boolean loyalty;
    private boolean eduComission;
    private boolean loanProviderFees;
    private float destinationAmount;
    private boolean initiatedByEducon;
    private String educonUserHashId;
    private boolean initiatedByRemitter;
    private String remitterUserHashId;
    private String email;
    private String educonEmail;
}
