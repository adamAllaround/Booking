package com.allaroundjava.booking.bookings.adapters.api

import com.allaroundjava.booking.IntegrationTestConfig
import com.allaroundjava.booking.bookings.config.BookingsConfig
import com.allaroundjava.booking.bookings.domain.model.Availability
import com.allaroundjava.booking.bookings.domain.model.Dates2020
import com.allaroundjava.booking.bookings.domain.model.OccupationEvent
import com.allaroundjava.booking.bookings.domain.ports.ItemsRepository
import com.allaroundjava.booking.bookings.domain.ports.OccupationRepository
import com.allaroundjava.booking.common.LoggingConfig
import com.allaroundjava.booking.common.events.EventsConfig
import com.allaroundjava.booking.common.events.HotelRoomCreatedEvent
import com.allaroundjava.booking.common.events.OwnerCreatedEvent
import com.allaroundjava.booking.notifications.BookingSuccessNotification
import com.allaroundjava.booking.notifications.NotificationRepository
import com.allaroundjava.booking.notifications.NotificationsConfig
import com.allaroundjava.booking.notifications.owners.OwnersRepository
import io.zonky.test.db.AutoConfigureEmbeddedDatabase
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.context.ApplicationEventPublisher
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus
import org.springframework.test.context.jdbc.Sql
import spock.lang.Specification
import spock.util.concurrent.PollingConditions

import java.time.Instant
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
    public static final UUID OWNER_ID = UUID.randomUUID()
    @Autowired
    private ItemsRepository itemsRepository

    @Autowired com.allaroundjava.booking.notifications.items.ItemsRepository notificationItemsRepository

    @Autowired
    private OccupationRepository occupationRepository

    @Autowired
    private TestRestTemplate restTemplate

    @Autowired
    private OwnersRepository notificationOwnersRepository

    @Autowired
    private NotificationRepository notificationRepository

    @Autowired
    private ApplicationEventPublisher publisher

    private PollingConditions pollingConditions = new PollingConditions(timeout: 6)

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
        existsOwner()
        existsItem()
        availabilities.forEach({
            availability ->
                existsAvailability(availability)
        })
    }

    void existsOwner() {
        publisher.publishEvent(new OwnerCreatedEvent(UUID.randomUUID(), Instant.now(), OWNER_ID, "ownerEmail@email.com"))
    }

    private void existsAvailability(Availability availability) {
        occupationRepository.handle(new OccupationEvent.AddAvailabilitySuccess(ITEM_ID, [availability]))
    }

    private void existsItem() {
        publisher.publishEvent(new HotelRoomCreatedEvent(UUID.randomUUID(),
                OWNER_ID,
                Dates2020.may(20).hour(12),
                ITEM_ID,
                OffsetTime.of(15, 0, 0, 0, ZoneOffset.UTC),
                OffsetTime.of(10, 0, 0, 0, ZoneOffset.UTC)))
    }

    private static HttpEntity<BookingRequest> bookingRequestFor(Collection<Availability> availabilities) {

        BookingRequest request = new BookingRequest(itemId: availabilities.first().getItemId(),
                firstName: "Test",
                lastName: "Test",
                email: "test@email.com",
                start: OffsetDateTime.ofInstant(availabilities.first().getInterval().start, ZoneOffset.UTC),
                end: OffsetDateTime.ofInstant(availabilities.first().getInterval().end, ZoneOffset.UTC),
                availabilities: availabilities*.id
        )
        return new HttpEntity<BookingRequest>(request)
    }

    void bookingNotificationExists(UUID bookingId) {
        pollingConditions.eventually {
            assert notificationRepository.allUnsent()
                    .any {it instanceof BookingSuccessNotification && bookingId == it.bookingId }
        }
    }
}

