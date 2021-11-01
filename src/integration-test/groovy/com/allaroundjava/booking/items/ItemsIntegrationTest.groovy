package com.allaroundjava.booking.items

import com.allaroundjava.booking.bookings.adapters.api.AddRoomController
import com.allaroundjava.booking.bookings.adapters.db.OwnersDatabaseRepository
import com.allaroundjava.booking.bookings.config.BookingsConfig
import com.allaroundjava.booking.common.events.EventsConfig
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
        classes = [EventsConfig, BookingsConfig])
@EnableAutoConfiguration
@AutoConfigureEmbeddedDatabase(provider = ZONKY, beanName = "dataSource")
@Sql("/events-db-creation.sql")
class ItemsIntegrationTest extends Specification {

    @Autowired
    private OwnersDatabaseRepository ownersDatabaseRepository
    @Autowired
    private TestRestTemplate testRestTemplate

    def "Should add item for existing user"() {
        def owner = new OwnersDatabaseRepository.OwnerDatabaseEntity(UUID.randomUUID(), "test name", "test email", LocalDateTime.of(2021, 5, 10, 10, 0).toInstant(ZoneOffset.UTC))
        given:
        ownersDatabaseRepository.save(owner)
        AddRoomController.AddRoomRequest item = new AddRoomController.AddRoomRequest(ownerId: owner.id, name: "Super room", capacity: 3, location: "Warsaw",
        hotelHourStart: OffsetTime.of(15,0,0,0,ZoneOffset.UTC) , hotelHourEnd: OffsetTime.of(12,0,0,0,ZoneOffset.UTC))

        def request = new HttpEntity<AddRoomController.AddRoomRequest>(item, new HttpHeaders(contentType: MediaType.APPLICATION_JSON))

        when:
        def entity = testRestTemplate.postForEntity(URI.create("/owners/${owner.id}/items"), request, AddRoomController.AddRoomResponse)

        then:
        entity.statusCode == HttpStatus.CREATED
        and:
        entity.body.capacity == 3
    }
}
