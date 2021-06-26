package com.allaroundjava.booking.bookings.config;

import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Log4j2
@Aspect
public class LoggingAspect {

    @Before("execution(public * com.allaroundjava.booking.bookings.domain.ports.*Service.*(..))")
    public void beforeServicePublicMethod(JoinPoint joinPoint) {
        log.info("Calling {}", joinPoint.toShortString());
    }

    @AfterReturning(pointcut = "execution(public * com.allaroundjava.booking.bookings.domain.ports.*Service.*(..))",
    returning = "returnValue")
    public void afterServicePublicMethod(Object returnValue) {
        log.info("Returning {}", returnValue.toString());
    }

    @AfterThrowing(pointcut = "execution(public * com.allaroundjava.booking.bookings.domain.ports.*Service.*(..))",
            throwing = "exception")
    public void afterServicePublicMethodThrows(Throwable exception) {
        log.error("Exception thrown within service method call", exception);
    }
}
