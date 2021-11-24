package com.allaroundjava.booking.bookings.adapters.api;

import com.allaroundjava.booking.bookings.domain.ports.OccupationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@AllArgsConstructor
class OccupationFacade {
    private final OccupationService occupationService;
    public Optional<AvailabilitiesResponse> save(UUID itemId, AvailabilityRequest request) {
        return occupationService.addAvailabilities(itemId, request.toDomainWithItemId(itemId))
                .map(success -> AvailabilitiesResponse.from(success.getAvailabilityList()))
                .map(Optional::of)
                .getOrElse(Optional::empty);
    }

    public Optional<BookingResponse> saveBooking(BookingRequest request) {
        return occupationService.addBooking(request.toDomain())
                .map(success -> BookingResponse.withBookingId(success.getBookingId()))
                .map(Optional::of)
                .getOrElse(Optional::empty);
    }

    Optional<UUID> save(BasketController.CreateBasketRequest createBasketRequest) {
        return occupationService.addBasket(createBasketRequest.getItemId(), createBasketRequest.getDateStart(), createBasketRequest.getDateEnd())
                .map(success -> Optional.of(success.getBasketId()))
                .getOrElse(Optional::empty);
    }
}
