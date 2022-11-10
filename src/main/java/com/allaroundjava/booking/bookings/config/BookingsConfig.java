package com.allaroundjava.booking.bookings.config;

import com.allaroundjava.booking.bookings.application.AddingCustomerDetails;
import com.allaroundjava.booking.bookings.application.AddingPaymentDetails;
import com.allaroundjava.booking.bookings.application.InitializingReservation;
import com.allaroundjava.booking.bookings.application.SearchService;
import com.allaroundjava.booking.bookings.availability.RoomAvailability;
import com.allaroundjava.booking.bookings.details.ReservationDetails;
import com.allaroundjava.booking.bookings.pricing.PaymentService;
import com.allaroundjava.booking.bookings.pricing.PricingService;
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
@ComponentScan(basePackages = {"com.allaroundjava.booking.bookings.adapters",
        "com.allaroundjava.booking.bookings.availability",
        "com.allaroundjava.booking.bookings.details",
        "com.allaroundjava.booking.bookings.pricing"})
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
    Clock clock() {
        return Clock.systemUTC();
    }

    @Bean
    LoggingAspect loggingAspect() {
        return new LoggingAspect();
    }
}
