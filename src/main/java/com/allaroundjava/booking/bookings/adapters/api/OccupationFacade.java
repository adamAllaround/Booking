package com.allaroundjava.booking.bookings.adapters.api;

import com.allaroundjava.booking.bookings.domain.command.AddAvailabilityCommand;
import com.allaroundjava.booking.bookings.domain.command.AddBasketCommand;
import com.allaroundjava.booking.bookings.domain.command.BookCommand;
import com.allaroundjava.booking.bookings.domain.model.Basket;
import com.allaroundjava.booking.bookings.domain.ports.OccupationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@AllArgsConstructor
class OccupationFacade {
    private final OccupationService occupationService;
    public Optional<AvailabilitiesResponse> save(AddAvailabilityCommand addAvailabilityCommand) {
        return occupationService.addAvailabilities(addAvailabilityCommand)
                .map(success -> AvailabilitiesResponse.from(success.getAvailabilityList()))
                .map(Optional::of)
                .getOrElse(Optional::empty);
    }

    Optional<BasketController.AddBasketResponse> save(AddBasketCommand addBasketCommand) {
        Basket basket = Basket.createNew(addBasketCommand.getRoomId(), addBasketCommand.getInterval());
        return occupationService.addBasket(basket)
                .map(success -> BasketController.AddBasketResponse.from(success.getBasketId(), success.getInterval()))
                .map(Optional::of)
                .getOrElse(Optional::empty);
    }

//    Optional<BookingsResponse> save(BookCommand bookCommand) {
//        return occupationService.book(bookCommand.getBasketId(), bookCommand.getCustomer())
//                .map(success -> BasketController.AddBasketResponse.from(success.getBasketId(), success.getInterval()))
//                .map(Optional::of)
//                .getOrElse(Optional::empty);
//    }
}
