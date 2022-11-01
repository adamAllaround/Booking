package com.allaroundjava.booking.bookings.application

import com.allaroundjava.booking.*
import com.allaroundjava.booking.bookings.application.SearchService
import com.allaroundjava.booking.bookings.config.BookingsConfig
import com.allaroundjava.booking.bookings.shared.Money
import com.allaroundjava.booking.common.LoggingConfig
import io.zonky.test.db.AutoConfigureEmbeddedDatabase
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.test.context.jdbc.Sql
import spock.lang.Specification

import java.time.LocalDate

import static com.allaroundjava.booking.bookings.domain.model.Dates2020.march
import static io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = [BookingsConfig, IntegrationTestConfig, LoggingConfig])
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
    private ReservationFixtures reservationFixtures

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
        dbCleaner.cleanPrices()
        dbCleaner.cleanBookings()
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

    def "when room is available then should be searchable"() {
        given:
        existsReservation(ROOM_ID, march(10).get(), march(12).get())
        existsReservation(ROOM_ID, march(15).get(), march(17).get())

        when:
        def rooms = searchService.findAvailableRooms(OWNER_ID,
                march(12).get(),
                march(15).get(),
                2)

        then:
        rooms.size() == 1
        rooms[0].name == "Szarotka"
        rooms[0].capacity == 3
        rooms[0].price == new Money(BigDecimal.valueOf(960), Currency.getInstance("PLN"))

    }

    def "when room unavailable in part of the period then its not searchable"() {
        given:
        existsReservation(ROOM_ID, march(10).get(), march(12).get())
        existsReservation(ROOM_ID, march(15).get(), march(17).get())

        when:
        def rooms = searchService.findAvailableRooms(OWNER_ID,
                march(11).get(),
                march(14).get(),
                3)

        then:
        rooms.isEmpty()
    }

    void existsRoom2() {
        roomFixtures.existsRoom(ROOM_ID, OWNER_ID)
    }

    void roomHasPrice() {
        pricingFixtures.hasFlatPrice(ROOM_ID, new Money(BigDecimal.valueOf(320L), Currency.getInstance("PLN")))
    }

    void existsReservation(UUID roomId, LocalDate reservationStart, LocalDate reservationEnd) {
        reservationFixtures.roomIsBooked(roomId, reservationStart, reservationEnd)
    }
}
