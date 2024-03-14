package com.hiwipay.paymentgatewayservice.ipc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hiwipay.paymentgatewayservice.common.annotation.LogEntryExit;
import com.hiwipay.paymentgatewayservice.common.dto.response.PersonDto;
import com.hiwipay.paymentgatewayservice.common.error.BadRequestException;
import com.hiwipay.paymentgatewayservice.common.restRequest.restTemplate.ForwardRequestService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserIpcService {
    private final ForwardRequestService forwardRequestService;
    private final ObjectMapper objectMapper;
    private final Logger logger = LoggerFactory.getLogger(UserIpcService.class);
    private static final String USER_RESP = "User response {}";

    @SneakyThrows
    @LogEntryExit
    public String getUserHashIdByEmail(String email){
        Map<String,String> emailRequest = new HashMap<>();
        emailRequest.put("studentOrUserEmailId", email);
        ResponseEntity<String> response = forwardRequestService
                .requestToUserService("/user/fetch-user-hash-id",emailRequest);

        if(response.getStatusCode() == HttpStatus.NO_CONTENT){
            return null;
        }
        emailRequest = objectMapper.readValue(response.getBody(), Map.class);

        return emailRequest.get("userHashId");
    }

    @SneakyThrows
    @LogEntryExit
    public Map<String, PersonDto> getPayersDataForStudent(Map<String, Object> relativeRequest) throws JsonProcessingException {
        logger.info("fetching relatives details by requests {}", relativeRequest);
        ResponseEntity<String> response = forwardRequestService.requestToUserService
                ("/user/relationship/get-relative-details", relativeRequest);

        return objectMapper.readValue
                (response.getBody(), new TypeReference<Map<String, PersonDto>>() {});
    }

    @SneakyThrows
    @LogEntryExit
    public Map<String, Map<String, PersonDto>> getPayersInfo(List<Map<String, String>> payerDataRequest) {
        ResponseEntity<String> response = forwardRequestService.requestToUserService
                ("/user/get-payers-userInfo", payerDataRequest);

        logger.info(USER_RESP, response.getStatusCode());

        return objectMapper.readValue(response.getBody(),
                new TypeReference<Map<String, Map<String, PersonDto>>>() {});
    }
    
}