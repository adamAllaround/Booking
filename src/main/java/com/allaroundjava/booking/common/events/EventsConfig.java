package com.allaroundjava.booking.common.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@EnableAspectJAutoProxy
public class EventsConfig {

    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @Bean
    EventStore eventStore(NamedParameterJdbcTemplate jdbcTemplate) {
        return new DatabaseEventStore(jdbcTemplate, objectMapper);
    }

    @Bean
    EventPublisher eventPublisher(ApplicationEventPublisher applicationEventPublisher, EventStore eventStore) {
        return new StoreEventPublisherDecorator(new JustForwardEventPublisher(applicationEventPublisher), eventStore);
    }
}


