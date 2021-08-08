package com.allaroundjava.booking.owners

import com.allaroundjava.booking.IntegrationTestConfig
import com.allaroundjava.booking.common.LoggingConfig
import com.allaroundjava.booking.common.events.EventsConfig
import com.allaroundjava.booking.items.ItemsConfig
import com.allaroundjava.booking.notifications.NotificationsConfig
import io.zonky.test.db.AutoConfigureEmbeddedDatabase
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.test.context.jdbc.Sql
import spock.lang.Specification
import spock.util.concurrent.PollingConditions

import static io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = [IntegrationTestConfig, EventsConfig, OwnersConfig, ItemsConfig, LoggingConfig, NotificationsConfig])
@EnableAutoConfiguration
@AutoConfigureEmbeddedDatabase(provider = ZONKY, beanName = "dataSource")
@Sql("/events-db-creation.sql")
class OwnersCrossModuleIntegrationTest extends Specification {

    @Autowired
    private TestRestTemplate testRestTemplate

    @Autowired
    private OwnersRepository ownersRepository

    @Autowired
    private com.allaroundjava.booking.items.OwnersRepository itemsModuleOwnersRepo

    @Autowired
    private com.allaroundjava.booking.notifications.owners.OwnersRepository notificationModuleOwnersRepo

    private PollingConditions pollingConditions = new PollingConditions(initialDelay: 2, timeout: 6)

    def "Should populate user in items ownersDbs and notifications"() {
        given: "A new Owner Request"
        def user = new OwnersController.OwnerRequest(name: "James", contact: "12346777")
        def request = new HttpEntity<OwnersController.OwnerRequest>(user, new HttpHeaders(contentType: MediaType.APPLICATION_JSON))
        when: "Creating the owner"
        def entity = testRestTemplate.postForEntity(URI.create("/owners"), request, OwnersController.OwnerResponse)
        then:
        entity.statusCode == HttpStatus.CREATED
        ownersModulePersistedOwner(entity.getBody().id)
        and:
        ownerSavedInItemsModule(entity)
        and:
        ownerSavedInNotificationsModule(entity)
    }

    private boolean ownersModulePersistedOwner(UUID id) {
        return ownersRepository.getSingle(id).isPresent()
    }

    private void ownerSavedInItemsModule(entity) {
        pollingConditions.eventually {
            assert itemsModuleOwnersRepo.getSingle(entity.getBody().id).isPresent()
        }
    }

    void ownerSavedInNotificationsModule(ResponseEntity<OwnersController.OwnerResponse> entity) {
        pollingConditions.eventually {
            assert notificationModuleOwnersRepo.findById(entity.getBody().id).isPresent()
        }
    }
}
