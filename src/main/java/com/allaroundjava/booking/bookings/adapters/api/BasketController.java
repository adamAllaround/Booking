package com.allaroundjava.booking.bookings.adapters.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/baskets")
@AllArgsConstructor
class BasketController {
    private final OccupationFacade occupation;

    @PostMapping
    ResponseEntity<Void> createBasket(@RequestBody CreateBasketRequest createBasketRequest) {
        return occupation.save(createBasketRequest)
                .map(resp -> ResponseEntity.noContent().location(getUri()).<Void>build())
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    private URI getUri() {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .build()
                .toUri();
    }

    @Data
    static class CreateBasketRequest {
        UUID itemId;
        OffsetDateTime dateStart;
        OffsetDateTime dateEnd;
    }
}
