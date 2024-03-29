package com.hiwipay.paymentgatewayservice.common.annotation;

import org.springframework.boot.logging.LogLevel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.temporal.ChronoUnit;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LogEntryExit {
    LogLevel value() default LogLevel.INFO;

    ChronoUnit unit() default ChronoUnit.SECONDS;

    boolean showArgs() default false;

    boolean showResult() default false;

    boolean showExecutionTime() default true;
}
