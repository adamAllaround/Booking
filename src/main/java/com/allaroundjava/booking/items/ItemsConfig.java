package com.allaroundjava.booking.items;

import com.allaroundjava.booking.common.events.EventPublisher;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Configuration
@EnableAspectJAutoProxy
public class ItemsConfig {

    @Bean
    OwnersRepository itemsOwnersRepository(JdbcTemplate jdbcTemplate) {
        return new OwnersRepository(jdbcTemplate);
    }

    @Bean
    ItemsRepository itemsRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        return new ItemsRepository(jdbcTemplate);
    }

    @Bean
    ItemsService itemsService(ItemsRepository itemsRepository, OwnersRepository ownersRepository, EventPublisher eventPublisher) {
        return new ItemsService(itemsRepository, ownersRepository, eventPublisher);
    }

    @Bean
    OwnerCreatedEventHandler ownerCreatedEventHandler(OwnersRepository ownersRepository) {
        return new OwnerCreatedEventHandler(ownersRepository);
    }

    @Bean
    ItemsController itemsController(ItemsService itemsService) {
        return new ItemsController(itemsService);
    }

    @Bean
    ItemsLoggingAspect itemsLoggingAspect() {
        return new ItemsLoggingAspect();
    }
}

@Log4j2
@Aspect
class ItemsLoggingAspect {

    @Before("execution(* com.allaroundjava.booking.items.*Service.*(..)) || execution(* com.allaroundjava.booking.items.*Repository.*(..))")
    public void serviceMethod(JoinPoint joinPoint) {
        log.info("Calling {}", joinPoint.toShortString());
    }

    @AfterReturning(pointcut = "execution(* com.allaroundjava.booking.items.*Service.*(..)) || execution(* com.allaroundjava.booking.items.*Repository.*(..))",
            returning = "returnValue")
    public void afterServicePublicMethod(Object returnValue) {
        log.info("Returning {}", returnValue.toString());
    }
}
