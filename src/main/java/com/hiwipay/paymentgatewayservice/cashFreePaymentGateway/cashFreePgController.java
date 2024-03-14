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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.cashfree.*;
import com.cashfree.model.*;
import com.hiwipay.paymentgatewayservice.cashFreePaymentGateway.dto.cashFreeCreateOrderRequestDto;
import com.hiwipay.paymentgatewayservice.common.annotation.LogEntryExit;
import com.hiwipay.paymentgatewayservice.common.annotation.LogRequest;
import com.hiwipay.paymentgatewayservice.common.dto.LedgerDto;
import com.hiwipay.paymentgatewayservice.common.dto.response.PersonDto;
import com.hiwipay.paymentgatewayservice.common.dto.resquest.GenericRequestBody;
import com.hiwipay.paymentgatewayservice.common.error.BadRequestException;
import com.hiwipay.paymentgatewayservice.common.error.ErrorType;
import com.hiwipay.paymentgatewayservice.ipc.TransactionIpcService;
import com.hiwipay.paymentgatewayservice.ipc.UserIpcService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/paymentgateway")
public class cashFreePgController {

    @Value("${cashfree.xCliendId}")
    private String xClientId;

    @Value("${cashfree.xClientSecret}")
    private String xClientSecret;
    private final Logger logger= LoggerFactory.getLogger(cashFreePgController.class);
    private final UserIpcService userIpcService;
    private final TransactionIpcService transactionIpcService;
    @GetMapping(value="/create")
    @LogEntryExit()
    @LogRequest
    public String welcome(){
        

        Cashfree.XClientId = xClientId;
        Cashfree.XClientSecret = xClientSecret;
        Cashfree.XEnvironment = Cashfree.SANDBOX;

        Cashfree cashfree = new Cashfree();
        String xApiVersion = "2023-08-01";

        CreateOrderRequest request = new CreateOrderRequest();
        request.setOrderAmount(1.00);// done
        request.setOrderCurrency("INR"); // done 
        request.setOrderId("order_23424533");// question?

        CustomerDetails customerDetails = new CustomerDetails();
        customerDetails.setCustomerId("walterwNrcMi"); // question?
        customerDetails.setCustomerPhone("8474090589");
        customerDetails.setCustomerName("Walter White");
        customerDetails.setCustomerEmail("walter.white@example.com");
        request.setCustomerDetails(customerDetails);

        OrderMeta orderMeta = new OrderMeta();
        orderMeta.setReturnUrl("https://www.cashfree.com/devstudio/preview/pg/web/checkout/?order_id={order_id}");
        orderMeta.setNotifyUrl("https://www.cashfree.com/devstudio/preview/pg/webhooks/42311861");
        orderMeta.setPaymentMethods("cc,dc,upi");
        request.setOrderMeta(orderMeta);

        request.setOrderExpiryTime("2024-03-15T04:37:35.538Z");

        request.setOrderNote("Sample Order Note");

        Map<String, String> orderTags = new HashMap<>();
        orderTags.put("name", "Developer");
        orderTags.put("company", "Cashfree");
        request.setOrderTags(orderTags);

        try {
            ApiResponse<OrderEntity> response = cashfree.PGCreateOrder(xApiVersion, request, null, null, null);
            System.out.println(response.getData());
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }
        return "HELLO";

    }

    @PostMapping(value="/createOrder" , consumes=MediaType.APPLICATION_JSON_VALUE ,produces = MediaType.APPLICATION_JSON_VALUE)
    @LogEntryExit()
    @LogRequest
    public String createOrder(@RequestBody GenericRequestBody<cashFreeCreateOrderRequestDto> genericRequestBody){
        cashFreeCreateOrderRequestDto details = genericRequestBody.getDetails().orElseThrow(() ->
                new BadRequestException(ErrorType.DETAILS_NOT_PRESENT.getMessage()));
        String hiwiId= details.getHiwiId();
        String payerUserHashId= details.getPayerUserHashId();

        // Important Check -> payment should be done by student and remitter only for testing we can allow admin 

        LedgerDto ledger = transactionIpcService.getLedger(hiwiId);
        float amountToPay=ledger.getAmount();
        String currencyCode=ledger.getCurrencyCodeSource();
        Map<String,String> map= new HashMap<>();
        map.put("payerType",ledger.getPayerType());
        map.put("hiwiRRN",ledger.getHiwiRrn());
        map.put("studentHashId",ledger.getUserHashId());

        List<Map<String, String>> result = new ArrayList<>();
        result.add(map);
        Map<String, Map<String, PersonDto>> personDtoMap = userIpcService.getPayersInfo(result);
        Map<String, PersonDto> payerDetails = personDtoMap.get(ledger.getHiwiRrn());

        if(payerDetails == null) throw new BadRequestException("No Payer is found");
        PersonDto personDto;
        if (payerDetails.get("relative")==null) {
            personDto = payerDetails.get("student");
        }
        else{
            personDto = payerDetails.get("relative");
        }
        
        return "HELLO WORLD";
    }
    

        
}
