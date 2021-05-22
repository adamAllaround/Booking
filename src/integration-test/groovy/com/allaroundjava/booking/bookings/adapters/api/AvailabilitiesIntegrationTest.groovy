package com.allaroundjava.booking.bookings.adapters.api

import com.allaroundjava.booking.DbCleaner
import com.allaroundjava.booking.IntegrationTestConfig
import com.allaroundjava.booking.bookings.config.BookingsConfig
import com.allaroundjava.booking.bookings.domain.model.Availability
import com.allaroundjava.booking.bookings.domain.model.OccupationEvent
import com.allaroundjava.booking.bookings.domain.ports.OccupationRepository
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
        classes = [BookingsConfig, IntegrationTestConfig])
@EnableAutoConfiguration
class AvailabilitiesIntegrationTest extends Specification {

    @Autowired
    private OccupationRepository occupationRepository

    @Autowired
    private TestRestTemplate testRestTemplate

    @Autowired
    private DbCleaner dbCleaner

    void cleanup() {
        dbCleaner.cleanAvailabilities()
    }

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
        existsAvailability(MAY10)

        def request = may11AvailabilityRequest()

        when:
        def entity = testRestTemplate.postForEntity(URI.create("/items/${ITEM_ID}/availabilities"), request, AvailabilityResponse)

        then:
        entity.statusCode == HttpStatus.CREATED
    }

    private existsAvailability(Availability availability) {
        occupationRepository.handle(new OccupationEvent.AddAvailabilitySuccess(ITEM_ID, availability))
    }

    HttpEntity<AvailabilityRequest> may11AvailabilityRequest() {
        def request = new AvailabilityRequest(start: OffsetDateTime.ofInstant(MAY11.start, ZoneOffset.UTC), end: OffsetDateTime.ofInstant(MAY11.end, ZoneOffset.UTC))
        return new HttpEntity<AvailabilityRequest>(request)
    }
}
