package com.allaroundjava.booking.bookings.config;

import com.allaroundjava.booking.bookings.application.PricingService;
import com.allaroundjava.booking.bookings.application.QueryForPrice;
import com.allaroundjava.booking.bookings.application.RoomAvailability;
import com.allaroundjava.booking.bookings.application.SearchService;
import com.allaroundjava.booking.bookings.domain.ports.ReservationPricesRepository;
import com.allaroundjava.booking.bookings.domain.ports.SlotRepository;
import com.allaroundjava.booking.bookings.domain.ports.PricingPolicyRepository;
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
    QueryForPrice queryForPrice(PricingPolicyRepository pricingPolicyRepository) {
        return new QueryForPrice(pricingPolicyRepository);
    }

    @Bean
    RoomAvailability roomAvailability(SlotRepository slotRepository) {
        return new RoomAvailability(slotRepository);
    }

    @Bean
    PricingService pricingService(QueryForPrice query, ReservationPricesRepository reservationPricesRepository) {
        return new PricingService(query, reservationPricesRepository);
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
