package com.allaroundjava.booking.common.events;

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

    @Bean
    EventStore eventStore(NamedParameterJdbcTemplate jdbcTemplate) {
        return new DatabaseEventStore(jdbcTemplate);
    }

    @Bean
    EventPublisher eventPublisher(ApplicationEventPublisher applicationEventPublisher, EventStore eventStore) {
        return new StoreEventPublisherDecorator(new JustForwardEventPublisher(applicationEventPublisher), eventStore);
    }
}


