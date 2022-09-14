package com.allaroundjava.booking.bookings.adapters.api

import com.allaroundjava.booking.AvailabilityFixtures
import com.allaroundjava.booking.DbCleaner
import com.allaroundjava.booking.IntegrationTestConfig
import com.allaroundjava.booking.PricingFixtures
import com.allaroundjava.booking.RoomFixtures
import com.allaroundjava.booking.bookings.application.SearchService
import com.allaroundjava.booking.bookings.config.BookingsConfig
import com.allaroundjava.booking.bookings.shared.Money
import com.allaroundjava.booking.common.LoggingConfig
import com.allaroundjava.booking.common.events.EventsConfig
import io.zonky.test.db.AutoConfigureEmbeddedDatabase
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import org.springframework.test.context.jdbc.Sql
import spock.lang.Ignore
import spock.lang.Specification

import java.time.Instant
import java.time.LocalDate
import java.time.Month

import static com.allaroundjava.booking.bookings.domain.model.Dates2020.march
import static io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = [BookingsConfig, IntegrationTestConfig, LoggingConfig, EventsConfig])
@EnableAutoConfiguration
@AutoConfigureEmbeddedDatabase(provider = ZONKY, beanName = "dataSource")
@Sql("/events-db-creation.sql")
class SearchingIntegrationTest extends Specification {

    public static final UUID ROOM_ID = UUID.randomUUID()
    public static final UUID OWNER_ID = UUID.randomUUID()

    @Autowired
    private RoomFixtures roomFixtures

    @Autowired
    private PricingFixtures pricingFixtures

    @Autowired
    private SearchService searchService

    @Autowired
    private TestRestTemplate testRestTemplate

    @Autowired
    private DbCleaner dbCleaner

    void setup() {
        existsRoom2()
        roomHasPrice()
    }

    void cleanup() {
        dbCleaner.cleanRooms()
    }

    def "when room is present then should be searchable"() {

        when:
        def rooms = searchService.findAvailableRooms(OWNER_ID,
                march(10).get(),
                march(12).get(),
                2)

        then:
        rooms.size() == 1
        rooms[0].name == "Szarotka"
        rooms[0].capacity == 3
        rooms[0].price == new Money(BigDecimal.valueOf(640L), Currency.getInstance("PLN"))
    }

    @Ignore
    def "when room is available then should be searchable"() {
        given:
        existsAvailability(ROOM_ID, march(10).hour(15), march(11).hour(10))

        when:
        def response = testRestTemplate.getForEntity("/owners/${OWNER_ID}/availabilities?dateFrom=${LocalDate.of(2020, Month.MARCH, 10)}&dateTo=${LocalDate.of(2020, Month.MARCH, 11)}",
                SearchController.FindAvailabilityResponse)

        then:
        response.statusCode == HttpStatus.OK
        response.body.roomsAvailable.size() == 1

    }

    @Ignore
    def "when room unavailable in whole period then its not searchable"() {
        given:
        existsAvailability(ROOM_ID, march(10).hour(15), march(11).hour(10))
        existsAvailability(ROOM_ID, march(12).hour(15), march(13).hour(10))

        when:
        def response = testRestTemplate.getForEntity("/owners/${OWNER_ID}/availabilities?dateFrom=${LocalDate.of(2020, Month.MARCH, 10)}&dateTo=${LocalDate.of(2020, Month.MARCH, 13)}",
                SearchController.FindAvailabilityResponse)

        then:
        response.statusCode == HttpStatus.OK
        response.body.roomsAvailable.size() == 0

    }

    void existsRoom() {
        roomFixtures.existsRoom(ROOM_ID, OWNER_ID)
    }

    void existsRoom2() {
        roomFixtures.existsRoom2(ROOM_ID, OWNER_ID)
    }

    void existsAvailability(UUID roomId, Instant from, Instant to) {
        availabilityFixtures.existsAvailability(roomId, from, to)
    }

    void roomHasPrice() {
        pricingFixtures.hasFlatPrice(ROOM_ID, new Money(BigDecimal.valueOf(320L), Currency.getInstance("PLN")))
    }
}
