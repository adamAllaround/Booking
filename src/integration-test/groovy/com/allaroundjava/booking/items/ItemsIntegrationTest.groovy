package com.allaroundjava.booking.items


import com.allaroundjava.booking.common.events.EventsConfig
import com.allaroundjava.booking.owners.OwnersConfig
import io.zonky.test.db.AutoConfigureEmbeddedDatabase
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.jdbc.Sql
import spock.lang.Specification

import java.time.LocalDateTime
import java.time.OffsetTime
import java.time.ZoneOffset

import static io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = [EventsConfig, OwnersConfig, ItemsConfig])
@EnableAutoConfiguration
@AutoConfigureEmbeddedDatabase(provider = ZONKY, beanName = "dataSource")
@Sql("/events-db-creation.sql")
class ItemsIntegrationTest extends Specification {
    @Autowired
    private TestRestTemplate testRestTemplate

    @Autowired
    private ItemsRepository itemsRepository

    @Autowired
    private OwnersRepository ownersRepository

    def "Should return all owners items"() {
        def owner = new Owner(UUID.randomUUID(), LocalDateTime.of(2021, 5, 10, 10, 0).toInstant(ZoneOffset.UTC))
        given:
        ownersRepository.save(owner)
        itemsRepository.save(new Item(ownerId: owner.id, name: "Some item", capacity: 10, location: "Warsaw", type: "test"))
        itemsRepository.save(new Item(ownerId: owner.id, name: "NY Apt", capacity: 3, location: "New York", type: "test"))
        when:

        def entity = testRestTemplate.getForEntity(URI.create("/owners/${owner.id}/items"), ItemsController.ItemsResponse)

        then:
        entity.statusCode == HttpStatus.OK

        and:
        entity.getBody().items.size() == 2
    }

    def "Should add item for existing user"() {
        def owner = new Owner(UUID.randomUUID(), LocalDateTime.of(2021, 5, 10, 10, 0).toInstant(ZoneOffset.UTC))
        given:
        ownersRepository.save(owner)
        ItemsController.ItemRequest item = new ItemsController.ItemRequest(name: "Some Item",
                details:
                        new ItemsController.HotelRoomDetails(capacity: 1,
                                location: "Test",
                                "hotelHourStart": OffsetTime.of(15,0,0,0,ZoneOffset.UTC),
                                "hotelHourEnd": OffsetTime.of(10,0,0,0,ZoneOffset.UTC)))
        def request = new HttpEntity<ItemsController.ItemRequest>(item, new HttpHeaders(contentType: MediaType.APPLICATION_JSON))

        when:
        def entity = testRestTemplate.postForEntity(URI.create("/owners/${owner.id}/items"), request, ItemsController.ItemResponse)

        then:
        entity.statusCode == HttpStatus.CREATED
        and:
        entity.body.capacity == 1
    }
}
