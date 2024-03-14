package com.hiwipay.paymentgatewayservice.common.dto.resquest;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.Valid;
import java.util.Optional;

@Data
@EqualsAndHashCode(callSuper = false)
public class GenericRequestBody<T> extends GenericRequest {
    private String detailUserRole;

    @Valid
    private Optional<T> details = Optional.empty();
}
