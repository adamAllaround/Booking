package com.allaroundjava.booking.items;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
public class ItemsConfig {

    @Bean
    OwnersRepository itemsOwnersRepository(JdbcTemplate jdbcTemplate) {
        return new OwnersRepository(jdbcTemplate);
    }

    @Bean
    OwnerCreatedEventHandler ownerCreatedEventHandler(OwnersRepository ownersRepository) {
        return new OwnerCreatedEventHandler(ownersRepository);
    }
}
