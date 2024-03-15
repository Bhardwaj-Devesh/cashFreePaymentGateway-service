package com.hiwipay.paymentgatewayservice.cashFreePaymentGateway;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import static com.hiwipay.paymentgatewayservice.common.pagination.PaginationService.roundOffDecimal;

import com.cashfree.*;
import com.cashfree.model.*;
import com.hiwipay.paymentgatewayservice.cashFreePaymentGateway.dto.CashFreeCreateOrderRequestDto;
import com.hiwipay.paymentgatewayservice.common.annotation.LogEntryExit;
import com.hiwipay.paymentgatewayservice.common.annotation.LogRequest;
import com.hiwipay.paymentgatewayservice.common.dto.LedgerDto;
import com.hiwipay.paymentgatewayservice.common.dto.response.PersonDto;
import com.hiwipay.paymentgatewayservice.common.dto.resquest.GenericRequestBody;
import com.hiwipay.paymentgatewayservice.common.error.BadRequestException;
import com.hiwipay.paymentgatewayservice.common.error.ErrorType;
import com.hiwipay.paymentgatewayservice.common.types.TxnStatusType;
import com.hiwipay.paymentgatewayservice.ipc.TransactionIpcService;
import com.hiwipay.paymentgatewayservice.ipc.UserIpcService;
import com.hiwipay.paymentgatewayservice.cashFreePaymentGateway.dto.CreateOrderEntryRequest;
@RestController
@RequiredArgsConstructor
@RequestMapping("/paymentgateway")
public class CashFreePgController {

    @Value("${cashfree.xCliendId}")
    private String xClientId;

    @Value("${cashfree.xClientSecret}")
    private String xClientSecret;

    String xApiVersion = "2023-08-01";
    private final Logger logger= LoggerFactory.getLogger(CashFreePgController.class);
    private final UserIpcService userIpcService;
    private final TransactionIpcService transactionIpcService;

    @PostMapping(value="/createOrder" , consumes=MediaType.APPLICATION_JSON_VALUE ,produces = MediaType.APPLICATION_JSON_VALUE)
    @LogEntryExit()
    @LogRequest
    public ResponseEntity<?> createOrder(@RequestBody GenericRequestBody<CashFreeCreateOrderRequestDto> genericRequestBody){
        CashFreeCreateOrderRequestDto details = genericRequestBody.getDetails().orElseThrow(() ->
                new BadRequestException(ErrorType.DETAILS_NOT_PRESENT.getMessage()));
        String hiwiId= details.getHiwiId();

        // Important Check -> payment should be done by student and remitter only for testing we can allow admin 

        List<LedgerDto> ledgerResponseList = transactionIpcService.getLedger(Collections.singletonList(hiwiId));
        LedgerDto ledger= ledgerResponseList.get(0);
        if(ledger.getStatus() == TxnStatusType.REFUNDED || ledger.getStatus() == TxnStatusType.SUCCESSFUL || ledger.getStatus() == TxnStatusType.CANCELLED || ledger.getStatus() == TxnStatusType.EXPIRED)  {
            throw new BadRequestException("This Transaction Code is already Processed");
        }
        if(ledger.getStatus() != TxnStatusType.COMPLETED){
            throw new BadRequestException("You are not allowed to perform this Transaction");
        }
        Map<String,String> map= new HashMap<>();
        map.put("payerType",ledger.getPayerType());
        map.put("hiwiRRN",ledger.getHiwiRrn());
        map.put("studentHashId",ledger.getUserHashId());

        Map<String, Map<String, PersonDto>> personDtoMap = userIpcService.getPayersInfo(Collections.singletonList(map));
        Map<String, PersonDto> payerDetails = personDtoMap.get(ledger.getHiwiRrn());

        if(payerDetails == null){
            throw new BadRequestException("No Payer is found for hiwi {}" + hiwiId);
        } 
        PersonDto personDto = payerDetails.get("relative") != null ? payerDetails.get("relative") : payerDetails.get("student");

        Cashfree.XClientId = xClientId;
        Cashfree.XClientSecret = xClientSecret;
        Cashfree.XEnvironment = Cashfree.SANDBOX;

        Cashfree cashfree = new Cashfree();

        CreateOrderRequest request = new CreateOrderRequest();
        request.setOrderAmount(roundOffDecimal(Double.valueOf(ledger.getAmount()), 2)); 
        request.setOrderCurrency(ledger.getCurrencyCodeSource());
        request.setOrderId("order_23424001");

        CustomerDetails customerDetails = new CustomerDetails();
        //customer_details.customer_id : should be alpha numeric and may contain underscore or hypens
        customerDetails.setCustomerId("walterwNrcMi"); 
        customerDetails.setCustomerPhone((personDto.getMobile().length() <= 10) ? personDto.getMobile() : personDto.getMobile().substring(personDto.getMobile().length() - 10));
        customerDetails.setCustomerName(personDto.getFirstName() +" "+ personDto.getLastName());
        customerDetails.setCustomerEmail(personDto.getEmail());
        request.setCustomerDetails(customerDetails);

        OrderMeta orderMeta = new OrderMeta();
        orderMeta.setReturnUrl("https://www.cashfree.com/devstudio/preview/pg/web/checkout/?order_id={order_id}");
        orderMeta.setNotifyUrl("https://www.cashfree.com/devstudio/preview/pg/webhooks/42311861");
        orderMeta.setPaymentMethods("cc,dc,upi");
        request.setOrderMeta(orderMeta);

        request.setOrderExpiryTime("2024-03-17T04:37:35.538Z");

        request.setOrderNote("Sample Order Note");

        Map<String, String> orderTags = new HashMap<>();
        orderTags.put("name", "Developer");
        orderTags.put("company", "Cashfree");
        request.setOrderTags(orderTags);

        try {
            ApiResponse<OrderEntity> response = cashfree.PGCreateOrder(xApiVersion, request, null, null, null);
            logger.info("Create Order Is called");
            return ResponseEntity.status(response.getStatusCode()).body(response.getData());
        } catch (ApiException e) {
            throw new BadRequestException(e.getMessage());
        }
    }   
}
