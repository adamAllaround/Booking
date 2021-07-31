package com.allaroundjava.booking.notifications;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Configuration
public class NotificationsConfig {
    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();


    @Bean
    NotificationRepository notificationRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        return new NotificationRepository(jdbcTemplate, objectMapper);
    }

    @Bean
    BookingSuccessEventHandler bookingSuccessEventHandler(NotificationRepository repository) {
        return new BookingSuccessEventHandler(repository);
    }
}
