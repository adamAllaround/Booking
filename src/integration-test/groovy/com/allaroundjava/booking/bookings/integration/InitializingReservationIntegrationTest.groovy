package com.allaroundjava.booking.bookings.integration

import com.allaroundjava.booking.DbCleaner
import com.allaroundjava.booking.IntegrationTestConfig
import com.allaroundjava.booking.PricingFixtures
import com.allaroundjava.booking.ReservationFixtures
import com.allaroundjava.booking.bookings.application.InitializeReservationCommand
import com.allaroundjava.booking.bookings.application.InitializingReservation
import com.allaroundjava.booking.bookings.config.BookingsConfig
import com.allaroundjava.booking.bookings.shared.Money
import com.allaroundjava.booking.common.LoggingConfig
import io.zonky.test.db.AutoConfigureEmbeddedDatabase
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.jdbc.Sql
import spock.lang.Specification

import java.time.LocalDate

import static io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY

@EnableAutoConfiguration
@ContextConfiguration(classes = [BookingsConfig, IntegrationTestConfig, LoggingConfig])
@AutoConfigureEmbeddedDatabase(provider = ZONKY, beanName = "dataSource")
@Sql("/events-db-creation.sql")
class InitializingReservationIntegrationTest extends Specification {
    public static final UUID ROOM_ID = UUID.randomUUID()

    @Autowired
    private PricingFixtures pricingFixtures

    @Autowired
    private ReservationFixtures reservationFixtures

    @Autowired
    private DbCleaner dbCleaner

    @Autowired
    private InitializingReservation initializingReservation

    void setup() {
        roomHasPrice()
    }

    void cleanup() {
        dbCleaner.cleanPrices()
        dbCleaner.cleanBookings()
    }

    def "When room available then reservation can be initialized"() {
        given:
        def dateFrom = LocalDate.of(2022,10,1)
        def dateTo = LocalDate.of(2022,10,10)

        when:
        def result = initializingReservation.initialize(new InitializeReservationCommand(ROOM_ID, dateFrom, dateTo, 3))

        then:
        result.isPresent()
    }

    def "When room not available then reservation is not initialized"() {
        given:
        def dateFrom = LocalDate.of(2022,10,1)
        def dateTo = LocalDate.of(2022,10,10)

        and:
        existsReservation(ROOM_ID, dateFrom, LocalDate.of(2022, 10, 6))

        when:
        def result = initializingReservation.initialize(new InitializeReservationCommand(ROOM_ID, dateFrom, dateTo, 3))

        then:
        result.isEmpty()
    }

    void roomHasPrice() {
        pricingFixtures.hasFlatPrice(ROOM_ID, new Money(BigDecimal.valueOf(320L), Currency.getInstance("PLN")))
    }

    void existsReservation(UUID roomId, LocalDate reservationStart, LocalDate reservationEnd) {
        reservationFixtures.roomIsBooked(roomId, reservationStart, reservationEnd)
    }

}
