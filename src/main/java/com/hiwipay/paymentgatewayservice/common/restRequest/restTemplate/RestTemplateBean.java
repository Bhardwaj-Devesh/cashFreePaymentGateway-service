package com.hiwipay.paymentgatewayservice.common.restRequest.restTemplate;

import com.hiwipay.paymentgatewayservice.common.restRequest.encryption.AESUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class RestTemplateBean {
    private RestTemplate restTemplate;

    @Bean
    public RestTemplate restTemplate() {
        restTemplate = new RestTemplate();
        return restTemplate;
    }

    @SneakyThrows
    public ResponseEntity<String> postRequestToUri(URI uri, Object body) {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.set("key", AESUtil.encryptInputString("hiwipaySymmetricKey"));

        RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
        if (attrs != null) {
            HttpServletRequest request = ((ServletRequestAttributes) attrs).getRequest();
            requestHeaders.set("Authorization", request.getHeader("Authorization"));
            requestHeaders.set("role", request.getHeader("role"));
        }

        HttpEntity<?> httpEntity = new HttpEntity<>(body, requestHeaders);
        HttpMethod method = HttpMethod.POST;

        ResponseEntity<String> responseEntity;
        try {
            responseEntity = restTemplate.exchange(uri, method, httpEntity, String.class);
        } catch (HttpClientErrorException e) {
            responseEntity = new ResponseEntity<>(e.getResponseBodyAsString(), HttpStatus.BAD_REQUEST);
        } catch (HttpServerErrorException e) {
            responseEntity = new ResponseEntity<>(e.getResponseBodyAsString(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            responseEntity = new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }

    @SneakyThrows
    public ResponseEntity<String> getRequestToExternalUrl(String url, Map<String, ?> uriVariables) {
        ResponseEntity<String> responseEntity;
        try {
            responseEntity = restTemplate.getForEntity(url, String.class, uriVariables);
        } catch (HttpClientErrorException e) {
            responseEntity = new ResponseEntity<>(e.getResponseBodyAsString(), HttpStatus.BAD_REQUEST);
        } catch (HttpServerErrorException e) {
            responseEntity = new ResponseEntity<>(e.getResponseBodyAsString(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            responseEntity = new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }
}