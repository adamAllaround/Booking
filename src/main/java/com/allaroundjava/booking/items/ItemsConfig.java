package com.allaroundjava.booking.items;

import com.allaroundjava.booking.common.events.EventPublisher;
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
    ItemsService itemsService(ItemsRepository itemsRepository, OwnersRepository ownersRepository, EventPublisher eventPublisher) {
        return new ItemsService(itemsRepository, ownersRepository, eventPublisher);
    }

    @Bean
    OwnerCreatedEventHandler ownerCreatedEventHandler(OwnersRepository ownersRepository) {
        return new OwnerCreatedEventHandler(ownersRepository);
    }

//    @Bean
//    ItemsController itemsController(ItemsService itemsService) {
//        return new ItemsController(itemsService);
//    }
}
