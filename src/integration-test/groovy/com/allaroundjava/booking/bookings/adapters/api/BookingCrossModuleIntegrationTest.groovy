package com.allaroundjava.booking.bookings.adapters.api

import com.allaroundjava.booking.IntegrationTestConfig
import com.allaroundjava.booking.bookings.config.BookingsConfig
import com.allaroundjava.booking.bookings.domain.model.Availability
import com.allaroundjava.booking.bookings.domain.model.Dates2020
import com.allaroundjava.booking.bookings.domain.model.ItemType
import com.allaroundjava.booking.bookings.domain.model.OccupationEvent
import com.allaroundjava.booking.bookings.domain.ports.ItemsRepository
import com.allaroundjava.booking.bookings.domain.ports.OccupationRepository
import com.allaroundjava.booking.common.LoggingConfig
import com.allaroundjava.booking.common.events.EventsConfig
import com.allaroundjava.booking.notifications.BookingSuccessEvent
import com.allaroundjava.booking.notifications.NotificationRepository
import com.allaroundjava.booking.notifications.NotificationsConfig
import io.zonky.test.db.AutoConfigureEmbeddedDatabase
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus
import org.springframework.test.context.jdbc.Sql
import spock.lang.Specification
import spock.util.concurrent.PollingConditions

import java.time.OffsetDateTime
import java.time.OffsetTime
import java.time.ZoneOffset

import static com.allaroundjava.booking.bookings.domain.model.AvailabilityFixture.ITEM_ID
import static com.allaroundjava.booking.bookings.domain.model.AvailabilityFixture.MAY10
import static io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = [BookingsConfig, IntegrationTestConfig, LoggingConfig, EventsConfig, NotificationsConfig])
@EnableAutoConfiguration
@AutoConfigureEmbeddedDatabase(provider = ZONKY, beanName = "dataSource")
@Sql("/events-db-creation.sql")
class BookingCrossModuleIntegrationTest extends Specification {
    @Autowired
    private ItemsRepository itemsRepository

    @Autowired
    private OccupationRepository occupationRepository

    @Autowired
    private TestRestTemplate restTemplate

    @Autowired
    private NotificationRepository notificationRepository

    private PollingConditions pollingConditions = new PollingConditions(initialDelay: 2, timeout: 6)

    def "Booking Notification added after booking done"() {
        given:
        existAvailabilities([MAY10])

        def request = bookingRequestFor([MAY10])

        when:
        def response = restTemplate.postForEntity(URI.create("/items/${ITEM_ID}/bookings"), request, BookingResponse)

        then:
        response.statusCode == HttpStatus.CREATED

        and:
        bookingNotificationExists(response.body.id)
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
                start: OffsetDateTime.ofInstant(availabilities.first().getInterval().start, ZoneOffset.UTC),
                end: OffsetDateTime.ofInstant(availabilities.first().getInterval().end, ZoneOffset.UTC),
                availabilities: availabilities*.id
        )
        return new HttpEntity<BookingRequest>(request)
    }

    void bookingNotificationExists(UUID bookingId) {
        pollingConditions.eventually {
            assert notificationRepository.all()
                    .any {it instanceof BookingSuccessEvent && bookingId.equals(it.bookingId)}
        }
    }
}

