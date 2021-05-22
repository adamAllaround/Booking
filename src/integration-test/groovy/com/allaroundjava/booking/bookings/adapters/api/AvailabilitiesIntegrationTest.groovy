package com.allaroundjava.booking.bookings.adapters.api

import com.allaroundjava.booking.bookings.config.BookingsConfig
import com.allaroundjava.booking.bookings.domain.model.Dates2020
import com.allaroundjava.booking.bookings.domain.model.OccupationEvent
import com.allaroundjava.booking.bookings.domain.ports.OccupationRepository
import com.allaroundjava.booking.common.events.EventsConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus
import spock.lang.Specification

import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset

import static com.allaroundjava.booking.bookings.domain.model.AvailabilityFixture.ITEM_ID
import static com.allaroundjava.booking.bookings.domain.model.AvailabilityFixture.MAY10
import static com.allaroundjava.booking.bookings.domain.model.AvailabilityFixture.MAY11
import static com.allaroundjava.booking.bookings.domain.model.AvailabilityFixture.MAY11
import static com.allaroundjava.booking.bookings.domain.model.AvailabilityFixture.MAY11

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = [EventsConfig, BookingsConfig])
@EnableAutoConfiguration
class AvailabilitiesIntegrationTest extends Specification {

    @Autowired
    private OccupationRepository occupationRepository

    @Autowired
    private TestRestTemplate testRestTemplate

    def "Should return all item availabilities"() {
        given:
        may20Availability()

        when:
        def entity = testRestTemplate.getForEntity(URI.create("/items/${ITEM_ID}/availabilities"), AvailabilitiesResponse)

        then:
        entity.statusCode == HttpStatus.OK

        and:
        entity.getBody().availabilities.size() == 1
    }

    private may20Availability() {
        occupationRepository.handle(new OccupationEvent.AddAvailabilitySuccess(ITEM_ID, MAY10))
    }

    def "Should add availability"() {
        given:
        may20Availability()
        and:
        def request = may21AvailabilityRequest()

        when:
        def entity = testRestTemplate.postForEntity(URI.create("/items/${ITEM_ID}/availabilities"), request, AvailabilityResponse)

        then:
        entity.statusCode == HttpStatus.CREATED
    }

    HttpEntity<AvailabilityRequest> may21AvailabilityRequest() {
        def request = new AvailabilityRequest(start: OffsetDateTime.ofInstant(MAY11.start, ZoneOffset.UTC), end: OffsetDateTime.ofInstant(MAY11.end, ZoneOffset.UTC))
        return new HttpEntity<AvailabilityRequest>(request)
    }
}
