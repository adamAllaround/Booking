package com.allaroundjava.booking.owners

import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

import static org.hamcrest.CoreMatchers.containsString
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class OwnersControllerTest extends Specification {
    private OwnersService ownersService = Mock()
    private OwnersController ownersController
    private MockMvc mockMvc
    private Owner owner = new Owner(id: 1, name: "Josh", contact: "56603982")

    void setup() {
        ownersController = new OwnersController(ownersService)
        this.mockMvc = MockMvcBuilders.standaloneSetup(ownersController).build()
    }

    def "Get all owners"() {
        when: "Owner is returned by repository"
        ownersService.getAll() >> [owner]

        then: "Expecting http 200"
        mockMvc.perform(get("/owners").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(content().string(containsString("Josh")))
    }

    def "Get single owner when owner found"() {
        when: "Owner is returned by repository"
        ownersService.getSingle(1L) >> Optional.of(owner)

        then: "Expecting http 200"
        mockMvc.perform(get("/owners/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(content().string(containsString("Josh")))
    }

    def "Get single owner when owner not present"() {
        when: "Owner is returned by repository"
        ownersService.getSingle(1L) >> Optional.empty()

        then: "Expecting http 200"
        mockMvc.perform(get("/owners/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
    }

    def "Create Owner"() {
        when: "Owner returned by repository"
        ownersService.save(_ as Owner) >> owner

        then: "Expecting http 201"
        mockMvc.perform(post("/owners").contentType(MediaType.APPLICATION_JSON)
                .content('{"name": "James", "contact":"123455"}'))
                .andExpect(status().is(HttpStatus.CREATED.value()))
    }
}
