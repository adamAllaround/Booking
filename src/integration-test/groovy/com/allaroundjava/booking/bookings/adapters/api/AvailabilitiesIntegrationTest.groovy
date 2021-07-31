package com.allaroundjava.booking.bookings.adapters.api

import com.allaroundjava.booking.DbCleaner
import com.allaroundjava.booking.IntegrationTestConfig
import com.allaroundjava.booking.bookings.config.BookingsConfig
import com.allaroundjava.booking.bookings.domain.model.Availability
import com.allaroundjava.booking.bookings.domain.model.Dates2020
import com.allaroundjava.booking.bookings.domain.model.ItemType
import com.allaroundjava.booking.bookings.domain.model.OccupationEvent
import com.allaroundjava.booking.bookings.domain.ports.AvailabilitiesRepository
import com.allaroundjava.booking.bookings.domain.ports.ItemsRepository
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
class AvailabilitiesIntegrationTest extends Specification {

    @Autowired
    private OccupationRepository occupationRepository

    @Autowired
    private ItemsRepository itemsRepository

    @Autowired
    private TestRestTemplate testRestTemplate

    @Autowired
    private AvailabilitiesRepository availabilitiesRepository

    @Autowired
    private DbCleaner dbCleaner

    void setup() {
        addItem()
    }

    void cleanup() {
        dbCleaner.cleanAvailabilities()
        dbCleaner.cleanItems()
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
        def entity = testRestTemplate.postForEntity(URI.create("/items/${ITEM_ID}/availabilities"), request, AvailabilitiesResponse)

        then:
        entity.statusCode == HttpStatus.CREATED

        and:
        availabilitiesExistInDb(entity.body.availabilities*.id)
    }

    def "Should add multiple availabilities"() {
        given:

        def request = may10toMay12AvailabilityRequest()

        when:
        def entity = testRestTemplate.postForEntity(URI.create("/items/${ITEM_ID}/availabilities"), request, AvailabilitiesResponse)

        then:
        entity.statusCode == HttpStatus.CREATED

        and:
        availabilitiesExistInDb(entity.body.availabilities*.id)
    }

    private addItem() {
        itemsRepository.saveNew(ITEM_ID,
                Dates2020.may(20).hour(12),
                ItemType.HotelRoom,
                OffsetTime.of(15, 0, 0, 0, ZoneOffset.UTC),
                OffsetTime.of(10, 0, 0, 0, ZoneOffset.UTC))
    }

    private existsAvailability(Availability availability) {
        occupationRepository.handle(new OccupationEvent.AddAvailabilitySuccess(ITEM_ID, [availability]))
    }

    HttpEntity<AvailabilityRequest> may11AvailabilityRequest() {
        def request = new AvailabilityRequest(start: OffsetDateTime.ofInstant(MAY11.start, ZoneOffset.UTC), end: OffsetDateTime.ofInstant(MAY11.end, ZoneOffset.UTC))
        return new HttpEntity<AvailabilityRequest>(request)
    }

    HttpEntity<AvailabilityRequest> may10toMay12AvailabilityRequest() {
        def request = new AvailabilityRequest(start: OffsetDateTime.ofInstant(MAY10.start, ZoneOffset.UTC), end: OffsetDateTime.ofInstant(MAY12.end, ZoneOffset.UTC))
        return new HttpEntity<AvailabilityRequest>(request)
    }

    void availabilitiesExistInDb(ArrayList<UUID> uuids) {
        uuids.forEach({
            assert availabilitiesRepository.getSingle(it).isPresent()
        })
    }
}
