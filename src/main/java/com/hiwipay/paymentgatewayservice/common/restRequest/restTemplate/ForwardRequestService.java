package com.hiwipay.paymentgatewayservice.common.restRequest.restTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hiwipay.paymentgatewayservice.common.error.BadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URI;

@Service
@RequiredArgsConstructor
public class ForwardRequestService {
    private final RestTemplateBean restTemplateBean;
    private final ObjectMapper objectMapper;
    private final Logger logger = LoggerFactory.getLogger(ForwardRequestService.class);


    @Value("${educon.service.host}")
    private String educonHost;
    @Value("${educon.service.port}")
    private int educonPort;
    @Value("${user.service.host}")
    private String userHost;
    @Value("${user.service.port}")
    private int userPort;
    @Value("${university.service.host}")
    private String universityHost;
    @Value("${university.service.port}")
    private int universityPort;
    @Value("${remittance.service.host}")
    private String remittanceHost;
    @Value("${remittance.service.port}")
    private int remittancePort;

    @Value("${transaction.service.host}")
    private String transactionHost;
    @Value("${transaction.service.port}")
    private int transactionPort;
      
    @SneakyThrows
    public ResponseEntity<String> requestToEduconService(String uriPath, Object body) {
        URI uri = new URI("http", null,
                educonHost, educonPort, uriPath, null, null);
        ResponseEntity<String> response = restTemplateBean.postRequestToUri(uri, body);
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new BadRequestException(response.getBody());
        }
        return response;
    }

    @SneakyThrows
    public ResponseEntity<String> requestToUserService(String uriPath, Object body) {
        URI uri = new URI("http", null,
                userHost, userPort, uriPath, null, null);
        ResponseEntity<String> response = restTemplateBean.postRequestToUri(uri, body);
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new BadRequestException(response.getBody());
        }
        return response;
    }

    @SneakyThrows
    public ResponseEntity<String> requestToUniversityService(String uriPath, Object body) {
        URI uri = new URI("http", null,
                universityHost, universityPort, uriPath, null, null);
        ResponseEntity<String> response = restTemplateBean.postRequestToUri(uri, body);
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new BadRequestException(response.getBody());
        }
        return response;
    }

    @SneakyThrows
    public ResponseEntity<String> requestToRemittanceService(String uriPath, Object body) {
        URI uri = new URI("http", null,
                remittanceHost, remittancePort, uriPath, null, null);
        ResponseEntity<String> response = restTemplateBean.postRequestToUri(uri, body);
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new BadRequestException(response.getBody());
        }
        return response;
    }

    @SneakyThrows
    public ResponseEntity<String> requestToTransactionService(String uriPath, Object body) {
        URI uri = new URI("http", null,
                transactionHost, transactionPort, uriPath, null, null);
        ResponseEntity<String> response = restTemplateBean.postRequestToUri(uri, body);
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new BadRequestException(response.getBody());
        }
        return response;
    }

    @SneakyThrows
    public ResponseEntity<String> postToExternalUrl(String url, HttpHeaders headers, Object body) {
        HttpEntity<?> httpEntity = new HttpEntity<>(body, headers);
        return restTemplateBean.restTemplate().postForEntity(url, httpEntity, String.class);
    }

    

    
}
