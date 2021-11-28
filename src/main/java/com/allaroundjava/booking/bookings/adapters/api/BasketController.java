package com.allaroundjava.booking.bookings.adapters.api;

import com.allaroundjava.booking.bookings.domain.command.AddBasketCommand;
import com.allaroundjava.booking.bookings.domain.model.Interval;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@RestController
@RequestMapping("/baskets")
@AllArgsConstructor
class BasketController {
    private final OccupationFacade occupation;

    @PostMapping
    ResponseEntity<AddBasketResponse> createBasket(@RequestBody AddBasketRequest addBasketRequest) {
        return occupation.save(addBasketRequest.toCommand())
                .map(resp -> ResponseEntity.created(getUri(resp)).body(resp))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    private URI getUri(AddBasketResponse response) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.getBasketId())
                .toUri();
    }

    @Data
    static class AddBasketRequest {
        UUID roomId;
        OffsetDateTime dateStart;
        OffsetDateTime dateEnd;

        AddBasketCommand toCommand() {
            return new AddBasketCommand(roomId, new Interval(dateStart.toInstant(), dateEnd.toInstant()));
        }
    }

    @Value
    static class AddBasketResponse {
        UUID basketId;
        OffsetDateTime dateStart;
        OffsetDateTime dateEnd;

        static AddBasketResponse from(UUID basketId, Interval interval) {
            return new AddBasketResponse(basketId,
                    OffsetDateTime.ofInstant(interval.getStart(), ZoneOffset.UTC),
                    OffsetDateTime.ofInstant(interval.getEnd(), ZoneOffset.UTC));
        }
    }
}
