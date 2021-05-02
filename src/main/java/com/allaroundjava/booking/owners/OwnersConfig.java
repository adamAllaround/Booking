package com.allaroundjava.booking.owners;

import com.allaroundjava.booking.common.events.EventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OwnersConfig {
    @Bean
    OwnersRepository ownersRepository() {
        return new OwnersRepository();
    }

    @Bean
    OwnersService ownersService(OwnersRepository ownersRepository, EventPublisher eventPublisher) {
        return new OwnersService(ownersRepository, eventPublisher);

    }
}
