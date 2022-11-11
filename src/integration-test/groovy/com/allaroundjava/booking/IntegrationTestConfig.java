package com.allaroundjava.booking;

import com.allaroundjava.booking.assertion.CustomerDetailsAssert;
import com.allaroundjava.booking.assertion.PaymentDetailsAssert;
import com.allaroundjava.booking.assertion.ReservationAssert;
import com.allaroundjava.booking.bookings.details.ReservationDetails;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

@Configuration
public class IntegrationTestConfig {
    //dataSource bean is injected with @AutoConfigureEmbeddedDatabase annotation
    @Bean
    JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    DbCleaner dbCleaner(JdbcTemplate jdbcTemplate) {
        return new DbCleaner(jdbcTemplate);
    }

    @Primary
    @Bean
    Clock testClock() {
        return Clock.fixed(Instant.parse("2020-01-01T10:15:30.00Z"),
                ZoneOffset.UTC);
    }

    @Bean
    RoomFixtures roomFixtures(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        return new RoomFixtures(namedParameterJdbcTemplate);
    }

    @Bean
    PricingFixtures pricingFixtures(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        return new PricingFixtures(namedParameterJdbcTemplate);
    }

    @Bean
    ReservationFixtures reservationFixtures(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        return new ReservationFixtures(namedParameterJdbcTemplate);
    }

    @Bean
    ReservationDetailsFixtures reservationDetailsFixtures(ReservationDetails reservationDetails) {
        return new ReservationDetailsFixtures(reservationDetails);
    }

    @Bean
    PreBookingFixtures preBookingFixtures(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        return new PreBookingFixtures(namedParameterJdbcTemplate);
    }

    @Bean
    CustomerDetailsAssert customerDetailsAssert(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        return new CustomerDetailsAssert(namedParameterJdbcTemplate);
    }

    @Bean
    PaymentDetailsAssert paymentDetailsAssert(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        return new PaymentDetailsAssert(namedParameterJdbcTemplate);
    }

    @Bean
    ReservationAssert reservationAssert(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        return new ReservationAssert(namedParameterJdbcTemplate);
    }


}
