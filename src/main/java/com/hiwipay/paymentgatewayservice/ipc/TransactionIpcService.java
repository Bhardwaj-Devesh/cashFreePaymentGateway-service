package com.hiwipay.paymentgatewayservice.ipc;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hiwipay.paymentgatewayservice.common.annotation.LogEntryExit;
import com.hiwipay.paymentgatewayservice.common.dto.LedgerDto;
import com.hiwipay.paymentgatewayservice.common.restRequest.restTemplate.ForwardRequestService;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@Service
@RequiredArgsConstructor
public class TransactionIpcService {
    private final ForwardRequestService forwardRequestService;
    private final ObjectMapper objectMapper;
    private final Logger logger = LoggerFactory.getLogger(TransactionIpcService.class);
    private static final String TRANSACTION_RESP = "Transaction response {}";

    @SneakyThrows
    @LogEntryExit
    public List<LedgerDto> getLedger(List<String> hiwiIds) {
        ResponseEntity<String> response = forwardRequestService.requestToTransactionService
                ("/transaction/getLedger", hiwiIds);

        logger.info(TRANSACTION_RESP, response.getStatusCode());

        return objectMapper.readValue(response.getBody(),
                new TypeReference<List<LedgerDto>>() {});
    }
}
