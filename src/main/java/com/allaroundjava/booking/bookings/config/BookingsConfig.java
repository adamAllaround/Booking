package com.allaroundjava.booking.bookings.config;

import com.allaroundjava.booking.bookings.domain.model.BookingPolicies;
import com.allaroundjava.booking.bookings.domain.ports.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.time.Clock;

@Configuration
@EnableAspectJAutoProxy
@ComponentScan(basePackages = "com.allaroundjava.booking.bookings.adapters")
public class BookingsConfig {

    @Bean
    AvailabilitiesService availabilitiesService(AvailabilitiesRepository repository){
        return new AvailabilitiesService(repository);
    }

    @Bean
    OccupationService occupationService(OccupationRepository occupationRepository) {
        return new OccupationService(occupationRepository);
    }

    @Bean
    BookingsService bookingsService(BookingsRepository bookingsRepository) {
        return new BookingsService(bookingsRepository);
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
}
