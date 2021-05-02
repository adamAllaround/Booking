package com.allaroundjava.booking.items;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ItemsConfig {
    @Bean
    CreateOwnerEventHandler createOwnerEventHandler() {
        return new CreateOwnerEventHandler();
    }
}
