package com.allaroundjava.booking.owners;

import com.allaroundjava.booking.common.events.EventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
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
}
