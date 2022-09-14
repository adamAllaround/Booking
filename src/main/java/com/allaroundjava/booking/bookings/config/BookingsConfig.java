package com.allaroundjava.booking.bookings.config;

import com.allaroundjava.booking.bookings.application.AvailabilityService;
import com.allaroundjava.booking.bookings.application.PricingService;
import com.allaroundjava.booking.bookings.application.SearchService;
import com.allaroundjava.booking.bookings.adapters.db.RoomMetaReadModel;
import com.allaroundjava.booking.bookings.domain.model.pricing.PricingPolicyRepository;
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
    SearchService searchService(RoomMetaReadModel roomMetaReadModel, PricingService pricingService, AvailabilityService availabilityService) {
        return new SearchService(availabilityService, roomMetaReadModel, pricingService);
    }

    @Bean
    PricingService pricingService(PricingPolicyRepository pricingPolicyRepository) {
        return new PricingService(pricingPolicyRepository);
    }

    @Bean
    AvailabilityService availabilityService() {
        return new AvailabilityService();
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
