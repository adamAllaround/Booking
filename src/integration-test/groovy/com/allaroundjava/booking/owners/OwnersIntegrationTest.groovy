package com.allaroundjava.booking.owners

import com.allaroundjava.booking.IntegrationTestConfig
import com.allaroundjava.booking.bookings.adapters.api.AddOwnerController
import com.allaroundjava.booking.bookings.config.BookingsConfig
import com.allaroundjava.booking.common.LoggingConfig
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
import spock.util.concurrent.PollingConditions

import static io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = [IntegrationTestConfig, EventsConfig, LoggingConfig, BookingsConfig])
@EnableAutoConfiguration
@AutoConfigureEmbeddedDatabase(provider = ZONKY, beanName = "dataSource")
@Sql("/events-db-creation.sql")
class OwnersIntegrationTest extends Specification {

    @Autowired
    private TestRestTemplate testRestTemplate

    private PollingConditions pollingConditions = new PollingConditions(initialDelay: 2, timeout: 6)

    def "Should populate user in items ownersDbs and notifications"() {
        given: "A new Owner Request"
        def user = new AddOwnerController.OwnerRequest(name: "James", email: "test@owner.email")
        def request = new HttpEntity<AddOwnerController.OwnerRequest>(user, new HttpHeaders(contentType: MediaType.APPLICATION_JSON))
        when: "Creating the owner"
        def entity = testRestTemplate.postForEntity(URI.create("/owners"), request, AddOwnerController.OwnerResponse)
        then:
        entity.statusCode == HttpStatus.CREATED
    }
}
