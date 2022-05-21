package com.allaroundjava.booking.bookings.adapters.api;

import com.allaroundjava.booking.bookings.domain.command.AddBasketCommand;
import com.allaroundjava.booking.bookings.domain.model.Basket;
import com.allaroundjava.booking.bookings.domain.model.Interval;
import com.allaroundjava.booking.bookings.domain.ports.BasketRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.*;
import java.util.UUID;

@RestController
@RequestMapping("/baskets")
@AllArgsConstructor
class BasketController {
    private final OccupationFacade occupation;
    private final BasketRepository basketRepository;
    private final Clock clock;

    @PostMapping
    ResponseEntity<AddBasketResponse> createBasket(@RequestBody AddBasketRequest addBasketRequest) {
        return occupation.save(addBasketRequest.toCommand(clock))
                .map(resp -> ResponseEntity.created(getUri(resp)).body(resp))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @GetMapping("{basketId}")
    ResponseEntity<GetBasketResponse> getBasket(@PathVariable UUID basketId) {
        return basketRepository.getSingle(basketId)
                .map(GetBasketResponse::from)
                .map(ResponseEntity::ok)
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
        LocalDate dateStart;
        LocalDate dateEnd;

        AddBasketCommand toCommand(Clock clock) {
            return new AddBasketCommand(roomId, new Interval(dateStart.atStartOfDay(clock.getZone()).toInstant(), dateEnd.atStartOfDay(clock.getZone()).toInstant()));
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

    @Value
    static class GetBasketResponse {
        UUID basketId;
        UUID roomId;
        OffsetDateTime dateStart;
        OffsetDateTime dateEnd;

        static GetBasketResponse from(Basket basket) {
            return new GetBasketResponse(basket.getId(),
                    basket.getRoomId(),
                    OffsetDateTime.ofInstant(basket.getInterval().getStart(), ZoneOffset.UTC),
                    OffsetDateTime.ofInstant(basket.getInterval().getEnd(), ZoneOffset.UTC));
        }
    }
}
