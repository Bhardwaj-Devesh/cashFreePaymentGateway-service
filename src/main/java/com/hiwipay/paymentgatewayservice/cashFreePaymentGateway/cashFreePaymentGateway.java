package com.hiwipay.paymentgatewayservice.cashFreePaymentGateway;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder(toBuilder = true)
public class cashFreePaymentGateway {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(insertable = false, updatable = false, unique = true)
    private String id;

    private String hiwiId;

    private String payerUserHashId;

    private Double amountToPay;

    private Double cashFreeFees;
}
