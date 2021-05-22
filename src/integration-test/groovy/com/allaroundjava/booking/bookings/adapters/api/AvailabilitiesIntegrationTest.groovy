package com.allaroundjava.booking.bookings.adapters.api

import com.allaroundjava.booking.bookings.config.BookingsConfig
import com.allaroundjava.booking.bookings.domain.model.Availability
import com.allaroundjava.booking.bookings.domain.model.OccupationEvent
import com.allaroundjava.booking.bookings.domain.ports.OccupationRepository
import com.allaroundjava.booking.common.DatabaseConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus
import spock.lang.Specification

import java.time.OffsetDateTime
import java.time.ZoneOffset

import static com.allaroundjava.booking.bookings.domain.model.AvailabilityFixture.*

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = [DatabaseConfig, BookingsConfig])
@EnableAutoConfiguration
class AvailabilitiesIntegrationTest extends Specification {

    @Autowired
    private OccupationRepository occupationRepository

    @Autowired
    private TestRestTemplate testRestTemplate

    def "Should return all item availabilities"() {
        given:
        existsAvailability(MAY10)

        when:
        def entity = testRestTemplate.getForEntity(URI.create("/items/${ITEM_ID}/availabilities"), AvailabilitiesResponse)

        then:
        entity.statusCode == HttpStatus.OK

        and:
        entity.getBody().availabilities.size() == 1
    }

    def "Should add availability"() {
        given:
        existsAvailability(MAY11)

        def request = may12AvailabilityRequest()

        when:
        def entity = testRestTemplate.postForEntity(URI.create("/items/${ITEM_ID}/availabilities"), request, AvailabilityResponse)

        then:
        entity.statusCode == HttpStatus.CREATED
    }

    private existsAvailability(Availability availability) {
        occupationRepository.handle(new OccupationEvent.AddAvailabilitySuccess(ITEM_ID, availability))
    }

    HttpEntity<AvailabilityRequest> may12AvailabilityRequest() {
        def request = new AvailabilityRequest(start: OffsetDateTime.ofInstant(MAY12.start, ZoneOffset.UTC), end: OffsetDateTime.ofInstant(MAY12.end, ZoneOffset.UTC))
        return new HttpEntity<AvailabilityRequest>(request)
    }
}
