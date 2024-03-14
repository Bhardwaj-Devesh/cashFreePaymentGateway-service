package com.hiwipay.paymentgatewayservice.common.error;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.hiwipay.paymentgatewayservice.common.error.apierror.ApiError;

import javax.validation.constraints.NotNull;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
@Slf4j(topic = "rest_controller_exception")
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
                                                                          HttpHeaders headers, HttpStatus status, WebRequest request) {
        String error = ex.getParameterName() + " parameter is missing";
        logger.error(ex.getMessage());
        return buildResponseEntity(new ApiError(BAD_REQUEST, error));
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
            HttpMediaTypeNotSupportedException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        StringBuilder builder = new StringBuilder();
        builder.append(ex.getContentType());
        builder.append(" media type is not supported. Supported media types are ");
        ex.getSupportedMediaTypes().forEach(t -> builder.append(t).append(", "));
        logger.error(ex.getMessage());
        return buildResponseEntity(new ApiError(HttpStatus.UNSUPPORTED_MEDIA_TYPE, builder.substring(0, builder.length() - 2)));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        ApiError apiError = new ApiError(BAD_REQUEST);
        apiError.setMessage("Validation error");
        apiError.addValidationErrors(ex.getBindingResult().getFieldErrors());
        apiError.addValidationError(ex.getBindingResult().getGlobalErrors());
        logger.error(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(javax.validation.ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolation(
            javax.validation.ConstraintViolationException ex) {
        ApiError apiError = new ApiError(BAD_REQUEST);
        apiError.setMessage("Validation error");
        apiError.addValidationErrors(ex.getConstraintViolations());
        logger.error(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFound(
            EntityNotFoundException ex) {
        ApiError apiError = new ApiError(NOT_FOUND);
        apiError.setMessage(ex.getMessage());
        logger.error(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ServletWebRequest servletWebRequest = (ServletWebRequest) request;
        log.info("{} to {}", servletWebRequest.getHttpMethod(), servletWebRequest.getRequest().getServletPath());
        String error = "Malformed JSON request";
        logger.error(ex.getMessage());
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, error));
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String error = "Error writing JSON output";
        logger.error(ex.getMessage());
        return buildResponseEntity(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, error));
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(
            NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.error(ex.getMessage());
        ApiError apiError = new ApiError(BAD_REQUEST);
        apiError.setMessage(String.format("Could not find the %s method for URL %s",
                ex.getHttpMethod(), ex.getRequestURL()));
        apiError.setDebugMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(javax.persistence.EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFound(javax.persistence.EntityNotFoundException ex) {
        logger.error(ex.getMessage());
        return buildResponseEntity(new ApiError(HttpStatus.NOT_FOUND));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    protected ResponseEntity<Object> handleDataIntegrityViolation(DataIntegrityViolationException ex,
                                                                  WebRequest request) {
        logger.error(ex.getMessage());
        if (ex.getCause() instanceof ConstraintViolationException) {
            return buildResponseEntity(new ApiError(HttpStatus.CONFLICT, "Database error"));
        }
        return buildResponseEntity(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<Object> handleMethodArgumentTypeMismatch(@NotNull MethodArgumentTypeMismatchException ex,
                                                                      WebRequest request) {
        ApiError apiError = new ApiError(BAD_REQUEST);
        String simpleName;
        simpleName = ex.getRequiredType() == null ? "" : ex.getRequiredType().getSimpleName();
        apiError.setMessage(String.format("The parameter '%s' of value '%s' could not be converted to type '%s'",
                ex.getName(), ex.getValue(), simpleName));
        apiError.setDebugMessage(ex.getMessage());
        logger.error(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(IncorrectInputException.class)
    protected ResponseEntity<Object> handleGenericException(IncorrectInputException ex, WebRequest request) {
        ApiError apiError = new ApiError(BAD_REQUEST);
        apiError.setMessage(ex.getMessage());
        apiError.setDebugMessage(ex.getMessage());
        logger.error(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(NotFoundException.class)
    protected ResponseEntity<Object> handleGenericException(NotFoundException ex, WebRequest request) {
        ApiError apiError = new ApiError(OK);
        apiError.setMessage(ex.getMessage());
        apiError.setDebugMessage(ex.getMessage());
        logger.error(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(AlreadyPresentException.class)
    protected ResponseEntity<Object> handleGenericException(AlreadyPresentException ex, WebRequest request) {
        ApiError apiError = new ApiError(OK);
        apiError.setMessage(ex.getMessage());
        apiError.setDebugMessage(ex.getMessage());
        logger.error(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(SaveRemittanceException.class)
    protected ResponseEntity<Object> handleGenericException(SaveRemittanceException ex, WebRequest request) {
        ApiError apiError = new ApiError(BAD_REQUEST);
        apiError.setMessage(ex.getMessage());
        apiError.setDebugMessage(ex.getMessage());
        logger.error(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleGenericException(Exception ex, WebRequest request) {
        ApiError apiError = new ApiError(BAD_REQUEST);
        apiError.setMessage(ex.getMessage());
        apiError.setDebugMessage(ex.getMessage());
        logger.error(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        logger.error(apiError.getMessage());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(apiError, headers, apiError.getStatus());
    }

}