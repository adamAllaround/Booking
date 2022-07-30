package com.allaroundjava.booking.notifications

import com.allaroundjava.booking.IntegrationTestConfig
import com.allaroundjava.booking.bookings.config.BookingsConfig
import com.allaroundjava.booking.bookings.domain.model.OccupationEvent
import com.allaroundjava.booking.common.LoggingConfig
import com.allaroundjava.booking.common.events.EventsConfig
import com.allaroundjava.booking.notifications.items.HotelRoom
import com.allaroundjava.booking.notifications.items.ItemsRepository
import com.allaroundjava.booking.notifications.owners.Owner
import com.allaroundjava.booking.notifications.owners.OwnersRepository
import io.zonky.test.db.AutoConfigureEmbeddedDatabase
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.context.ApplicationEventPublisher
import org.springframework.test.context.jdbc.Sql
import spock.lang.Specification
import spock.util.concurrent.PollingConditions

import java.time.Instant
import java.time.LocalDateTime
import java.time.OffsetTime
import java.time.ZoneOffset

import static io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = [IntegrationTestConfig, EventsConfig, BookingsConfig, LoggingConfig, NotificationsConfig])
@EnableAutoConfiguration
@AutoConfigureEmbeddedDatabase(provider = ZONKY, beanName = "dataSource")
@Sql("/events-db-creation.sql")
class EventToNotificationTest extends Specification {

    @Autowired
    private OwnersRepository ownersRepository

    @Autowired
    private ItemsRepository itemsRepository

    @Autowired
    private MessageRepository messageRepository

    @Autowired
    private TestRestTemplate testRestTemplate

    @Autowired
    private ApplicationEventPublisher publisher

    private PollingConditions pollingConditions = new PollingConditions(timeout: 10)

    def "Notification is created on booking"() {
        given:
        Owner owner = createOwner()
        HotelRoom hotelRoom = hotelRoomFor(owner)
        def bookingSuccessEvent = bookingSuccessEvent(hotelRoom)

        when:
        eventPublished(bookingSuccessEvent)

        then:
        messagesCreated(bookingSuccessEvent.getEventId())

        and:
        messagesMarkedSent()
    }

    Owner createOwner() {
        Owner owner = new Owner(UUID.randomUUID(), "owner@email.com")
        ownersRepository.save(owner)
        return owner
    }

    HotelRoom hotelRoomFor(Owner owner) {
        HotelRoom hotelRoom = new HotelRoom(UUID.randomUUID(),
                owner.id,
                OffsetTime.of(15, 0, 0, 0, ZoneOffset.UTC),
                OffsetTime.of(15, 0, 0, 0, ZoneOffset.UTC))
        itemsRepository.save(hotelRoom)
        return hotelRoom
    }

    OccupationEvent.BookingSuccess bookingSuccessEvent(HotelRoom hotelRoom) {
        Instant start = LocalDateTime.of(2021, 8, 21, 10,0).toInstant(ZoneOffset.UTC)
        Instant end = LocalDateTime.of(2021, 8, 23, 10,0).toInstant(ZoneOffset.UTC)
        com.allaroundjava.booking.bookings.shared.Interval interval = new com.allaroundjava.booking.bookings.shared.Interval(start, end)
        return new OccupationEvent.BookingSuccess(UUID.randomUUID(), Instant.now(),
                UUID.randomUUID(), hotelRoom.getId(), interval, [UUID.randomUUID(), UUID.randomUUID()].toSet(), "booker@email.com")
    }

    void eventPublished(OccupationEvent.BookingSuccess event) {
        publisher.publishEvent(event)
    }

    void messagesCreated(UUID eventId) {
        pollingConditions.eventually {
            assert messageRepository.allUnsent().findAll {it.eventId == eventId}.size() == 2
        }
    }

    void messagesMarkedSent() {
        pollingConditions.eventually {
            assert messageRepository.allUnsent().isEmpty()
        }
    }
}
