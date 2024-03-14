package com.hiwipay.paymentgatewayservice.common.dto.resquest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class DynamicValues {
    private String name = "Student";
    private String para1;
    private String para2;
    private String para3;
    private String tynote;
}
