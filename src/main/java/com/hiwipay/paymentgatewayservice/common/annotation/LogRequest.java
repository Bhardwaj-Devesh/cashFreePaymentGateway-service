package com.hiwipay.paymentgatewayservice.common.annotation;

import org.springframework.boot.logging.LogLevel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.temporal.ChronoUnit;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LogRequest {
    LogLevel value() default LogLevel.INFO;
    boolean showArgs() default true;

}
