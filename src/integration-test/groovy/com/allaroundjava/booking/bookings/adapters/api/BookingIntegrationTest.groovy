package com.allaroundjava.booking.bookings.adapters.api

import com.allaroundjava.booking.DbCleaner
import com.allaroundjava.booking.IntegrationTestConfig
import com.allaroundjava.booking.bookings.config.BookingsConfig
import com.allaroundjava.booking.bookings.domain.model.Availability
import com.allaroundjava.booking.bookings.domain.model.Dates2020
import com.allaroundjava.booking.bookings.domain.model.ItemType
import com.allaroundjava.booking.bookings.domain.model.OccupationEvent
import com.allaroundjava.booking.bookings.domain.ports.ItemsRepository
import com.allaroundjava.booking.bookings.domain.ports.OccupationRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus
import spock.lang.Specification

import java.time.OffsetTime
import java.time.ZoneOffset

import static com.allaroundjava.booking.bookings.domain.model.AvailabilityFixture.*
import static com.allaroundjava.booking.bookings.domain.model.BookingFixture.fromSingleAvailability

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = [BookingsConfig, IntegrationTestConfig])
@EnableAutoConfiguration
class BookingIntegrationTest extends Specification {

    @Autowired
    private ItemsRepository itemsRepository

    @Autowired
    private TestRestTemplate restTemplate

    @Autowired
    private OccupationRepository occupationRepository

    @Autowired
    private DbCleaner dbCleaner

    void cleanup() {
        dbCleaner.cleanAvailabilities()
        dbCleaner.cleanItems()
        dbCleaner.cleanBookings()
    }

    def "Should return all bookings"() {
        given:
        existBookings([MAY10])

        when:
        def response = restTemplate.getForEntity(URI.create("/items/${ITEM_ID}/bookings"), BookingsResponse)

        then:
        response.statusCode == HttpStatus.OK

        and:
        response.getBody().getBookings().size() == 1
    }

    def "Should add booking"() {
        given:
        existAvailabilities([MAY10])

        def request = bookingRequestFor([MAY10])

        when:
        def response = restTemplate.postForEntity(URI.create("/items/${ITEM_ID}/bookings"), request, BookingResponse)

        then:
        response.statusCode == HttpStatus.CREATED
    }

    def "Should add booking on multiple availabilities"() {
        given:
        existAvailabilities([MAY10, MAY11, MAY12])

        def request = bookingRequestFor([MAY10, MAY11])

        when:
        def response = restTemplate.postForEntity(URI.create("/items/${ITEM_ID}/bookings"), request, BookingResponse)

        then:
        response.statusCode == HttpStatus.CREATED

    }

    private void existBookings(Collection<Availability> availabilities) {
        availabilities.forEach({ availability ->
            existsAvailability(availability)
            occupationRepository.handle(new OccupationEvent.BookingSuccess(ITEM_ID, fromSingleAvailability(availability)))
        })
    }

    private void existAvailabilities(Collection<Availability> availabilities) {
        existsItem()
        availabilities.forEach({
            availability ->
                existsAvailability(availability)
        })
    }

    private void existsAvailability(Availability availability) {
        occupationRepository.handle(new OccupationEvent.AddAvailabilitySuccess(ITEM_ID, [availability]))
    }

    private void existsItem() {
        itemsRepository.saveNew(ITEM_ID,
                Dates2020.may(20).hour(12),
                ItemType.HotelRoom,
                OffsetTime.of(15, 0, 0, 0, ZoneOffset.UTC),
                OffsetTime.of(10, 0, 0, 0, ZoneOffset.UTC))
    }

    private static HttpEntity<BookingRequest> bookingRequestFor(Collection<Availability> availabilities) {

        BookingRequest request = new BookingRequest(itemId: availabilities.first().getItemId(),
                firstName: "Test",
                lastName: "Test",
                interval: availabilities.first().getInterval(),
                availabilities: availabilities*.id
        )
        return new HttpEntity<BookingRequest>(request)
    }
}