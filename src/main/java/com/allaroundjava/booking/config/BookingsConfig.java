package com.allaroundjava.booking.config;

import com.allaroundjava.booking.application.*;
import com.allaroundjava.booking.domain.availability.RoomAvailability;
import com.allaroundjava.booking.domain.details.ReservationDetails;
import com.allaroundjava.booking.domain.pricing.PaymentService;
import com.allaroundjava.booking.domain.pricing.PricingService;
import com.allaroundjava.booking.domain.pricing.QueryForPrice;
import com.allaroundjava.booking.domain.readmodel.AvailabilitySearch;
import com.allaroundjava.booking.domain.readmodel.RoomMeta;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.time.Clock;

@Configuration
@EnableAspectJAutoProxy
@ComponentScan(basePackages = {"com.allaroundjava.booking.adapters",
        "com.allaroundjava.booking.domain.availability",
        "com.allaroundjava.booking.domain.details",
        "com.allaroundjava.booking.domain.pricing"})
public class BookingsConfig {

    @Bean
    SearchService searchService(RoomMeta roomMetaReadModel, QueryForPrice queryForPrice, AvailabilitySearch availabilityReadModel) {
        return new SearchService(availabilityReadModel, roomMetaReadModel, queryForPrice);
    }

    @Bean
    InitializingReservation initializingReservation(RoomAvailability roomAvailability, PricingService pricingService, ReservationDetails reservationDetails) {
        return new InitializingReservation(roomAvailability, pricingService, reservationDetails);
    }

    @Bean
    AddingCustomerDetails addingCustomerDetails(ReservationDetails reservationDetails) {
        return new AddingCustomerDetails(reservationDetails);
    }

    @Bean
    AddingPaymentDetails addingPaymentDetails(PaymentService paymentService, ReservationDetails reservationDetails) {
        return new AddingPaymentDetails(paymentService, reservationDetails);
    }

    @Bean
    ConfirmingReservation confirmingReservation(RoomAvailability roomAvailability, ReservationDetails reservationDetails) {
        return new ConfirmingReservation(roomAvailability, reservationDetails);
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
