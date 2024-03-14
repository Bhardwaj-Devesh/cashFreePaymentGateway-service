package com.hiwipay.paymentgatewayservice.common.dto.resquest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Attachment {

    private String type;

    private String name;

    private String content;
}
