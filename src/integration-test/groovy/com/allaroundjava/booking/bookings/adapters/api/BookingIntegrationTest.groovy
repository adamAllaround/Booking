package com.allaroundjava.booking.bookings.adapters.api

import com.allaroundjava.booking.DbCleaner
import com.allaroundjava.booking.IntegrationTestConfig
import com.allaroundjava.booking.bookings.adapters.db.RoomsDatabaseRepository
import com.allaroundjava.booking.bookings.config.BookingsConfig
import com.allaroundjava.booking.bookings.domain.model.Availability
import com.allaroundjava.booking.bookings.domain.model.Dates2020
import com.allaroundjava.booking.bookings.domain.model.OccupationEvent
import com.allaroundjava.booking.bookings.domain.ports.AvailabilitiesRepository
import com.allaroundjava.booking.bookings.domain.ports.BookingsRepository
import com.allaroundjava.booking.bookings.domain.ports.OccupationRepository
import com.allaroundjava.booking.common.LoggingConfig
import com.allaroundjava.booking.common.events.EventsConfig
import io.zonky.test.db.AutoConfigureEmbeddedDatabase
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus
import org.springframework.test.context.jdbc.Sql
import spock.lang.Ignore
import spock.lang.Specification

import java.time.OffsetDateTime
import java.time.OffsetTime
import java.time.ZoneOffset

import static com.allaroundjava.booking.bookings.domain.model.AvailabilityFixture.*
import static io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = [BookingsConfig, IntegrationTestConfig, LoggingConfig, EventsConfig])
@EnableAutoConfiguration
@AutoConfigureEmbeddedDatabase(provider = ZONKY, beanName = "dataSource")
@Sql("/events-db-creation.sql")
@Ignore
class BookingIntegrationTest extends Specification {

    @Autowired
    private TestRestTemplate restTemplate

    @Autowired
    private OccupationRepository occupationRepository

    @Autowired
    private BookingsRepository bookingsRepository

    @Autowired
    private AvailabilitiesRepository availabilitiesRepository

    @Autowired
    private RoomsDatabaseRepository roomsDatabaseRepository

    @Autowired
    private DbCleaner dbCleaner

    void cleanup() {
        dbCleaner.cleanAvailabilities()
        dbCleaner.cleanRooms()
        dbCleaner.cleanBookings()
    }

    def "Should add booking"() {
        given:
        existAvailabilities([MAY10])

        def request = bookingRequestFor([MAY10])

        when:
        def response = restTemplate.postForEntity(URI.create("/items/${ITEM_ID}/bookings"), request, BookingResponse)

        then:
        response.statusCode == HttpStatus.CREATED

        and:
        bookingExistsInDatabase(response.body.id)

        and:
        availabilitiesMarkedBooked([MAY10])
    }

    def "Should add booking on multiple availabilities"() {
        given:
        existAvailabilities([MAY10, MAY11, MAY12])

        def request = bookingRequestFor([MAY10, MAY11])

        when:
        def response = restTemplate.postForEntity(URI.create("/items/${ITEM_ID}/bookings"), request, BookingResponse)

        then:
        response.statusCode == HttpStatus.CREATED

        and:
        bookingExistsInDatabase(response.body.id)

        and:
        availabilitiesMarkedBooked([MAY10,MAY11])

    }

    private void existAvailabilities(Collection<Availability> availabilities) {
        existsRoom()
        availabilities.forEach({
            availability ->
                existsAvailability(availability)
        })
    }

    private void existsAvailability(Availability availability) {
        occupationRepository.handle(new OccupationEvent.AddAvailabilitySuccess(ITEM_ID, [availability]))
    }

    private void existsRoom() {
        def room = new RoomsDatabaseRepository.RoomDatabaseEntity(ITEM_ID,
                UUID.randomUUID(),
                "test name",
                3,
                "test location",
                OffsetTime.of(15, 0, 0, 0, ZoneOffset.UTC),
                OffsetTime.of(10, 0, 0, 0, ZoneOffset.UTC),
                Dates2020.may(20).hour(12))
        roomsDatabaseRepository.save(room)
    }

    private static HttpEntity<BookingRequest> bookingRequestFor(Collection<Availability> availabilities) {

        BookingRequest request = new BookingRequest(itemId: availabilities.first().getItemId(),
                firstName: "Test",
                lastName: "Test",
                start: OffsetDateTime.ofInstant(availabilities.first().getInterval().start, ZoneOffset.UTC),
                end: OffsetDateTime.ofInstant(availabilities.first().getInterval().end, ZoneOffset.UTC),
                availabilities: availabilities*.id,
                email: "test@booker.email"
        )
        return new HttpEntity<BookingRequest>(request)
    }

    void bookingExistsInDatabase(UUID uuid) {
        assert bookingsRepository.getSingle(uuid).isPresent()
    }

    void availabilitiesMarkedBooked(Collection<Availability> availabilities) {
        availabilitiesRepository.getAllByIds(availabilities*.id).forEach({
            println "availability ${it.id} is ${it.booked}"
            assert it.booked
        })
    }
}
