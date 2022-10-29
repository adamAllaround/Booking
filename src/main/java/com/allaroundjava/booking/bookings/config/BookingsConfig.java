package com.allaroundjava.booking.bookings.config;

import com.allaroundjava.booking.bookings.application.SearchService;
import com.allaroundjava.booking.bookings.pricing.QueryForPrice;
import com.allaroundjava.booking.bookings.readmodel.AvailabilitySearch;
import com.allaroundjava.booking.bookings.readmodel.RoomMeta;
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
    SearchService searchService(RoomMeta roomMetaReadModel, QueryForPrice queryForPrice, AvailabilitySearch availabilityReadModel) {
        return new SearchService(availabilityReadModel, roomMetaReadModel, queryForPrice);
    }

    @Bean
    Clock clock() {
        return Clock.systemUTC();
    }

    @Bean
    LoggingAspect loggingAspect() {
        return new LoggingAspect();
    }
}
