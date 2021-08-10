package com.allaroundjava.booking.items

import com.allaroundjava.booking.IntegrationTestConfig
import com.allaroundjava.booking.bookings.config.BookingsConfig
import com.allaroundjava.booking.common.LoggingConfig
import com.allaroundjava.booking.common.events.EventsConfig
import com.allaroundjava.booking.notifications.NotificationsConfig
import io.zonky.test.db.AutoConfigureEmbeddedDatabase
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.test.context.jdbc.Sql
import spock.lang.Specification
import spock.util.concurrent.PollingConditions

import java.time.LocalDateTime
import java.time.OffsetTime
import java.time.ZoneOffset

import static io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = [IntegrationTestConfig, EventsConfig, ItemsConfig, BookingsConfig, LoggingConfig, NotificationsConfig])
@EnableAutoConfiguration
@AutoConfigureEmbeddedDatabase(provider = ZONKY, beanName = "dataSource")
@Sql("/events-db-creation.sql")
class ItemsCrossModuleIntegrationTest extends Specification {

    @Autowired
    private TestRestTemplate testRestTemplate

    @Autowired
    private OwnersRepository ownersRepository

    @Autowired
    private com.allaroundjava.booking.bookings.domain.ports.ItemsRepository itemsRepository

    @Autowired
    private com.allaroundjava.booking.notifications.items.ItemsRepository notificationItemsRepo

    private PollingConditions pollingConditions = new PollingConditions(initialDelay: 2, timeout: 6)

    def "When adding new item then Item present in Bookings module"() {
        given:
        Owner owner = createOwner()
        Item newItem = newItem(owner)

        when:
        def createResponse = createItemWithHttpPost(newItem)

        then:
        createResponse.statusCode == HttpStatus.CREATED

        and:
        itemPresentInBookingsModule(createResponse.getBody().getId())

        and:
        itemPresentInNotificationsModule(createResponse.getBody().getId())

    }

    Item newItem(Owner owner) {
        Item item = new Item()
        item.setId(UUID.randomUUID())
        item.setLocation("Test Location")
        item.setCapacity(20)
        item.setOwnerId(owner.getId())
        item.setName("Test Name")
        item.setHotelHourStart(OffsetTime.of(14, 0, 0, 0, ZoneOffset.UTC))
        item.setHotelHourEnd(OffsetTime.of(12, 0, 0, 0, ZoneOffset.UTC))
        return item
    }

    ResponseEntity<ItemsController.ItemResponse> createItemWithHttpPost(Item item) {
        def requestObject = new ItemsController.ItemRequest(ownerId: item.getOwnerId(),
                name: item.getName(),
                details:  new ItemsController.HotelRoomDetails(capacity: item.getCapacity(),
                        location: item.getLocation(),
                        hotelHourStart: item.getHotelHourStart(),
                        hotelHourEnd: item.getHotelHourEnd()))
        def request = new HttpEntity<ItemsController.ItemRequest>(requestObject)
        return testRestTemplate.postForEntity(URI.create("/owners/${item.getOwnerId()}/items"), request, ItemsController.ItemResponse)
    }

    void itemPresentInBookingsModule(UUID uuid) {
        pollingConditions.eventually {
            assert itemsRepository.findById(uuid).isPresent()
        }

    }

    Owner createOwner() {
        return ownersRepository.save(
                new Owner(UUID.randomUUID(),
                        LocalDateTime.of(2020, 5, 23, 9, 54, 0).toInstant(ZoneOffset.UTC)))
    }

    void itemPresentInNotificationsModule(UUID uuid) {
        pollingConditions.eventually {
            assert notificationItemsRepo.findById(uuid).isPresent()
        }
    }
}
