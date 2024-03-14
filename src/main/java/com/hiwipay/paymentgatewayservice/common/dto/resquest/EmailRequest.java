package com.hiwipay.paymentgatewayservice.common.dto.resquest;


import com.hiwipay.paymentgatewayservice.common.types.ServiceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class EmailRequest {
    private String email;

    @NotEmpty
    private String subject;

    @NotEmpty
    private String body;

    private String userHashId;

    private DynamicValues dynamicValues;

    private Attachment attachment;

    private ServiceType service;

    public void setEmail(String email) {
        this.email = email.toLowerCase();
    }
}
