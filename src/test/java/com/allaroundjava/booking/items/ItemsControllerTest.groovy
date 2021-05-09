package com.allaroundjava.booking.items

import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

import java.time.LocalDateTime
import java.time.ZoneOffset

import static org.hamcrest.CoreMatchers.containsString
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class ItemsControllerTest extends Specification {
    private ItemsRepository itemsRepository = Mock()
    private ItemsController itemsController
    private MockMvc mockMvc

    void setup() {
        itemsController = new ItemsController(itemsRepository)
        mockMvc = MockMvcBuilders.standaloneSetup(itemsController).build()
    }

    def "Get All Items for given Owner"() {
        given:
        Owner owner = new Owner(UUID.randomUUID(), LocalDateTime.of(2021, 5,11,10,0).toInstant(ZoneOffset.UTC))
        when:
        itemsRepository.getAllByOwnerId(owner.getId()) >> [new Item(UUID.randomUUID(), owner.getId(), "Some Item", 10, "Warsaw")]
        then:
        mockMvc.perform(MockMvcRequestBuilders.get("/owners/${owner.id}/items").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(content().string(containsString("Some Item")))
    }
}
