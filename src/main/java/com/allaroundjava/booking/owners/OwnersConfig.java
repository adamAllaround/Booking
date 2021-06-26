package com.allaroundjava.booking.owners;

import com.allaroundjava.booking.common.events.EventPublisher;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
@EnableAspectJAutoProxy
public class OwnersConfig {
    @Bean
    OwnersRepository ownersRepository(JdbcTemplate jdbcTemplate) {
        return new OwnersRepository(jdbcTemplate);
    }

    @Bean
    OwnersService ownersService(OwnersRepository ownersRepository, EventPublisher eventPublisher) {
        return new OwnersService(ownersRepository, eventPublisher);
    }

    @Bean
    OwnersController ownersController(OwnersService ownersService) {
        return new OwnersController(ownersService);
    }

    @Bean
    OwnersLoggingAspect ownersLoggingAspect() {
        return new OwnersLoggingAspect();
    }
}

@Log4j2
@Aspect
class OwnersLoggingAspect {

    @Before("execution(* com.allaroundjava.booking.owners.*Service.*(..)) || execution(* com.allaroundjava.booking.owners.*Repository.*(..))")
    public void serviceMethod(JoinPoint joinPoint) {
        log.info("Calling {}", joinPoint.toShortString());
    }

    @AfterReturning(pointcut = "execution(* com.allaroundjava.booking.owners.*Service.*(..)) || execution(* com.allaroundjava.booking.owners.*Repository.*(..))",
            returning = "returnValue")
    public void afterServicePublicMethod(Object returnValue) {
        log.info("Returning {}", returnValue.toString());
    }
}
