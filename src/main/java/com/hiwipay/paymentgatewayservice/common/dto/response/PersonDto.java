package com.hiwipay.paymentgatewayservice.common.dto.response;

import com.hiwipay.paymentgatewayservice.common.types.RelationshipType;

import lombok.Data;

@Data
public class PersonDto {
    private String userHashId;
    private String firstName;
    private String lastName;
    private String email;
    private String mobile;
    private RelationshipType relationshipToStudent;

    public void setEmail(String email) {
        if (email != null) {
            this.email = email.toLowerCase();
        }
    }
}
