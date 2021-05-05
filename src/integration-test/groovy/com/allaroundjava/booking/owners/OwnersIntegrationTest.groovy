package com.allaroundjava.booking.owners

import com.allaroundjava.booking.common.events.EventsConfig
import com.allaroundjava.booking.items.ItemsConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import spock.lang.Specification
import spock.util.concurrent.PollingConditions

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = [EventsConfig, OwnersConfig, ItemsConfig])
@EnableAutoConfiguration
class OwnersIntegrationTest extends Specification {

    @Autowired
    private TestRestTemplate testRestTemplate

    @Autowired
    private OwnersRepository ownersRepository

    @Autowired
    private com.allaroundjava.booking.items.OwnersRepository itemsModuleOwnersRepo

    PollingConditions pollingConditions = new PollingConditions(timeout: 6)

    def "Should populate user in items and ownersDbs"() {
        given: "A new Owner Request"
        def user = new OwnersController.OwnerRequest(name: "James", contact: "12346777")
        def request = new HttpEntity<OwnersController.OwnerRequest>(user, new HttpHeaders(contentType: MediaType.APPLICATION_JSON))
        when: "Creating the owner"
        def entity = testRestTemplate.postForEntity(URI.create("/owners"), request, OwnersController.OwnerResponse)
        then:
        entity.statusCode == HttpStatus.CREATED
        ownersModulePersistedOwner(entity.getBody().id)
        ownerSavedInItemsModule(entity)
    }

    private boolean ownersModulePersistedOwner(UUID id) {
        return ownersRepository.getSingle(id).isPresent()
    }

    private ownerSavedInItemsModule(entity) {
        pollingConditions.eventually {
            assert itemsModuleOwnersRepo.getSingle(entity.getBody().id).isPresent()
        }
    }
}
