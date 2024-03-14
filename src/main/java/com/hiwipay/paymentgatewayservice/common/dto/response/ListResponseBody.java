package com.hiwipay.paymentgatewayservice.common.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder(toBuilder = true)
public class ListResponseBody {

    private List<?> result;

    private String noOfRecord;

    private String noOfPages;

    private String pageSize;

    private String pageNo;

    private String totalRecords;

}
