package com.allaroundjava.booking.items;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Configuration
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
    ItemsService itemsService(ItemsRepository itemsRepository, OwnersRepository ownersRepository) {
        return new ItemsService(itemsRepository, ownersRepository);
    }

    @Bean
    OwnerCreatedEventHandler ownerCreatedEventHandler(OwnersRepository ownersRepository) {
        return new OwnerCreatedEventHandler(ownersRepository);
    }

    @Bean
    ItemsController itemsController(ItemsService itemsService) {
        return new ItemsController(itemsService);
    }
}
