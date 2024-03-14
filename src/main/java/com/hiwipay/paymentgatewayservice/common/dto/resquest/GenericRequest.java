package com.hiwipay.paymentgatewayservice.common.dto.resquest;

import com.hiwipay.paymentgatewayservice.common.pagination.PaginationRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@EqualsAndHashCode(callSuper = true)
@Data
public class GenericRequest extends PaginationRequest {

    @NotEmpty
    @Email(message = "Invalid email")
    private String email;
    @NotEmpty
    private String userHashId;
    private String role;
    private String deviceId;
    private boolean pagination = false;
    private String partnerReferenceId;
    public void setEmail(String email) {
        this.email = email.toLowerCase();
    }

}
