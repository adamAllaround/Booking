package com.allaroundjava.booking.bookings.application

import com.allaroundjava.booking.DbCleaner
import com.allaroundjava.booking.IntegrationTestConfig
import com.allaroundjava.booking.PreBookingFixtures
import com.allaroundjava.booking.ReservationFixtures
import com.allaroundjava.booking.assertion.ReservationAssert
import com.allaroundjava.booking.bookings.config.BookingsConfig
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
class ConfirmingReservationTest extends Specification {

    private static final UUID RESERVATION_ID = UUID.randomUUID()
    private static final UUID ROOM_ID = UUID.randomUUID()
    private static final LocalDate DATE_FROM = LocalDate.of(2022, 11, 12)
    private static final LocalDate DATE_TO = LocalDate.of(2022, 11, 15)

    @Autowired
    private ConfirmingReservation confirmingReservation

    @Autowired
    private ReservationAssert reservationAssert

    @Autowired
    private ReservationFixtures reservationFixtures

    @Autowired
    private PreBookingFixtures preBookingFixtures

    @Autowired
    private DbCleaner dbCleaner

    void cleanup() {
        dbCleaner.cleanPrices()
        dbCleaner.cleanBookings()
        dbCleaner.cleanPreBookings()
    }

    def "When room is available reservation can be confirmed"() {
        given:
        roomIsPreBooked()

        when:
        def result = confirmingReservation.confirm(RESERVATION_ID)

        then:
        result.isRight()
        reservationAssert.existsReservationFor(RESERVATION_ID, ROOM_ID)
        reservationAssert.reservedBetween(RESERVATION_ID, DATE_FROM, DATE_TO)
    }

    def "When room is booked reservation cannot be confirmed"() {
        given:
        roomIsPreBooked()

        and:
        roomIsBookedInTimeSlot()

        when:
        def result = confirmingReservation.confirm(RESERVATION_ID)

        then:
        result.isLeft()
        reservationAssert.noReservationFor(RESERVATION_ID, ROOM_ID)
    }

    void existsReservation(UUID roomId, LocalDate reservationStart, LocalDate reservationEnd) {
        reservationFixtures.roomIsBooked(roomId, reservationStart, reservationEnd)
    }

    void roomIsPreBooked() {
        preBookingFixtures.preBook(RESERVATION_ID, ROOM_ID, DATE_FROM, DATE_TO)
    }

    void roomIsBookedInTimeSlot() {
        reservationFixtures.roomIsBooked(ROOM_ID, LocalDate.of(2022, 11, 14), LocalDate.of(2022, 11, 18))
    }
}
