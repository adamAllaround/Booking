package com.allaroundjava.booking.bookings.adapters.api

import com.allaroundjava.booking.bookings.adapters.db.OwnersDatabaseRepository
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

import java.time.Instant

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class AddOwnerControllerTest extends Specification {
    private OwnersDatabaseRepository ownersRepository = Mock()
    private AddOwnerController ownersController
    private MockMvc mockMvc
    private OwnersDatabaseRepository.OwnerDatabaseEntity owner = new OwnersDatabaseRepository.OwnerDatabaseEntity(
            UUID.randomUUID(),
            "Josh",
            "test@owner.email",
            Instant.now())

    void setup() {
        ownersController = new AddOwnerController(ownersRepository)
        this.mockMvc = MockMvcBuilders.standaloneSetup(ownersController).build()
    }

    def "Create Owner"() {
        when: "Owner returned by repository"
        ownersRepository.save(_ as OwnersDatabaseRepository.OwnerDatabaseEntity) >> owner

        then: "Expecting http 201"
        mockMvc.perform(post("/owners").contentType(MediaType.APPLICATION_JSON)
                .content('{"name": "James", "email":"123455"}'))
                .andExpect(status().is(HttpStatus.CREATED.value()))
    }
}
