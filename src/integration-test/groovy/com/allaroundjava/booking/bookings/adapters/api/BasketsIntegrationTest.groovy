package com.allaroundjava.booking.bookings.adapters.api

import com.allaroundjava.booking.AvailabilityFixtures
import com.allaroundjava.booking.DbCleaner
import com.allaroundjava.booking.IntegrationTestConfig
import com.allaroundjava.booking.RoomFixtures
import com.allaroundjava.booking.bookings.config.BookingsConfig
import com.allaroundjava.booking.bookings.domain.model.Availability
import com.allaroundjava.booking.bookings.domain.ports.BasketRepository
import com.allaroundjava.booking.common.LoggingConfig
import com.allaroundjava.booking.common.events.EventsConfig
import io.zonky.test.db.AutoConfigureEmbeddedDatabase
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import org.springframework.test.context.jdbc.Sql
import spock.lang.Specification

import java.time.OffsetDateTime
import java.time.ZoneOffset

import static com.allaroundjava.booking.bookings.domain.model.AvailabilityFixture.*
import static io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = [BookingsConfig, IntegrationTestConfig, LoggingConfig, EventsConfig])
@EnableAutoConfiguration
@AutoConfigureEmbeddedDatabase(provider = ZONKY, beanName = "dataSource")
@Sql("/events-db-creation.sql")
class BasketsIntegrationTest extends Specification {

    @Autowired
    private AvailabilityFixtures availabilityFixtures

    @Autowired
    private RoomFixtures roomFixtures

    @Autowired
    private BasketRepository basketRepository

    @Autowired
    private DbCleaner dbCleaner

    @Autowired
    private TestRestTemplate testRestTemplate

    void setup() {
        existsRoom()
    }

    void cleanup() {
        dbCleaner.cleanAvailabilities()
        dbCleaner.cleanRooms()
    }

    def "Should add basket for single availability"() {
        given:
        existsAvailability(MAY10)

        def request = may10BasketRequest()

        when:
        def entity = testRestTemplate.postForEntity(URI.create("/baskets"),request, BasketController.AddBasketResponse)

        then:
        entity.statusCode == HttpStatus.CREATED

        and:
        !entity.headers.get("Location").isEmpty()

        and:
        basketExistsInDb(entity.body.basketId)

    }

    def "Should add basket for multiple availabilities"() {
        given:
        existsAvailability(MAY10)
        existsAvailability(MAY11)
        existsAvailability(MAY12)

        def request = may10to11BasketRequest()

        when:
        def entity = testRestTemplate.postForEntity(URI.create("/baskets"),request, BasketController.AddBasketResponse)

        then:
        entity.statusCode == HttpStatus.CREATED

        and:
        !entity.headers.get("Location").isEmpty()

        and:
        basketExistsInDb(entity.body.basketId)
    }

    def "Should add basket side by side"() {
        given:
        existsAvailability(MAY10)
        existsAvailability(MAY11)
        existsAvailability(MAY12)

        def request = may10to11BasketRequest()

        when:
        def firstBooking = testRestTemplate.postForEntity(URI.create("/baskets"),request, BasketController.AddBasketResponse)

        then:
        firstBooking.statusCode == HttpStatus.CREATED

        when:
        def nextRequest = may11ToMay12BasketRequest()

        and:
        def secondBooking = testRestTemplate.postForEntity(URI.create("/baskets"),nextRequest, BasketController.AddBasketResponse)

        then:
        secondBooking.statusCode == HttpStatus.CREATED

        and:
        basketExistsInDb(secondBooking.body.basketId)
    }

    private existsRoom() {
        roomFixtures.existsRoom(ITEM_ID, UUID.randomUUID())
    }

    private existsAvailability(Availability availability) {
        availabilityFixtures.existsAvailability(ITEM_ID, availability.getInterval().start, availability.getInterval().end)
    }

    BasketController.AddBasketRequest may10BasketRequest() {
        new BasketController.AddBasketRequest(roomId: ITEM_ID, dateStart: OffsetDateTime.ofInstant(MAY10.start, ZoneOffset.UTC), dateEnd: OffsetDateTime.ofInstant(MAY10.end, ZoneOffset.UTC))
    }

    BasketController.AddBasketRequest may10to11BasketRequest() {
        new BasketController.AddBasketRequest(roomId: ITEM_ID, dateStart: OffsetDateTime.ofInstant(MAY10.start, ZoneOffset.UTC), dateEnd: OffsetDateTime.ofInstant(MAY11.end, ZoneOffset.UTC))
    }

    BasketController.AddBasketRequest may11ToMay12BasketRequest() {
        new BasketController.AddBasketRequest(roomId: ITEM_ID, dateStart: OffsetDateTime.ofInstant(MAY11.start, ZoneOffset.UTC), dateEnd: OffsetDateTime.ofInstant(MAY12.end, ZoneOffset.UTC))
    }

    void basketExistsInDb(UUID basketId) {
        assert basketRepository.getSingle(basketId).isPresent()
    }
}
