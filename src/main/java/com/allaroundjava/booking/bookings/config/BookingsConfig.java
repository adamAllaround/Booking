package com.allaroundjava.booking.bookings.config;

import com.allaroundjava.booking.bookings.adapters.db.BasketDatabaseEntity;
import com.allaroundjava.booking.bookings.adapters.db.OwnersDatabaseRepository;
import com.allaroundjava.booking.bookings.adapters.db.RoomsDatabaseRepository;
import com.allaroundjava.booking.bookings.domain.model.BookingPolicies;
import com.allaroundjava.booking.bookings.domain.ports.*;
import com.allaroundjava.booking.common.events.EventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.time.Clock;

@Configuration
@EnableAspectJAutoProxy
@ComponentScan(basePackages = "com.allaroundjava.booking.bookings.adapters")
public class BookingsConfig {

    @Bean
    OccupationService occupationService(OccupationRepository occupationRepository, EventPublisher eventPublisher, BasketRepository basketRepository) {
        return new OccupationService(occupationRepository, eventPublisher, basketRepository);
    }

    @Bean
    Clock clock() {
        return Clock.systemUTC();
    }

    @Bean
    BookingPolicies bookingPolicies(Clock clock) {
        return BookingPolicies.allHotelRoomPolicies(clock);
    }

    @Bean
    LoggingAspect loggingAspect() {
        return new LoggingAspect();
    }

    @Bean
    OwnersDatabaseRepository ownersDatabaseRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        return new OwnersDatabaseRepository(namedParameterJdbcTemplate);
    }

    @Bean
    RoomsDatabaseRepository roomsDatabaseRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        return new RoomsDatabaseRepository(namedParameterJdbcTemplate);
    }
}
