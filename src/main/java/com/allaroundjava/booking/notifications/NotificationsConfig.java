package com.allaroundjava.booking.notifications;

import com.allaroundjava.booking.notifications.items.HotelRoomCreatedEventHandler;
import com.allaroundjava.booking.notifications.items.ItemsRepository;
import com.allaroundjava.booking.notifications.owners.OwnerCreatedEventHandler;
import com.allaroundjava.booking.notifications.owners.OwnersRepository;
import com.allaroundjava.booking.notifications.sending.EmailSender;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Configuration
public class NotificationsConfig {
    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();


    @Bean
    EventRepository notificationEventRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        return new EventRepository(jdbcTemplate, objectMapper);
    }

    @Bean
    BookingSuccessEventHandler bookingSuccessEventHandler(EventRepository repository) {
        return new BookingSuccessEventHandler(repository);
    }

    @Bean
    OwnersRepository notificationModuleOwnersRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        return new OwnersRepository(jdbcTemplate);
    }

    @Bean
    OwnerCreatedEventHandler notificationModuleOwnerCreatedHandler(OwnersRepository ownersRepository) {
        return new OwnerCreatedEventHandler(ownersRepository);
    }

    @Bean
    ItemsRepository notificationModuleItemsRepo(NamedParameterJdbcTemplate jdbcTemplate) {
        return new ItemsRepository(jdbcTemplate);
    }

    @Bean
    HotelRoomCreatedEventHandler notificationModuleHotelRoomCreatedHandler(ItemsRepository itemsRepository) {
        return new HotelRoomCreatedEventHandler(itemsRepository);
    }

    @Bean
    NotificationRepository notificationRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        return new NotificationRepository(jdbcTemplate, objectMapper);
    }

    @Bean
    NotificationPublisher notificationPublisher(NotificationRepository repository) {
        return new NotificationPublisher(repository, new EmailSender());
    }
}
