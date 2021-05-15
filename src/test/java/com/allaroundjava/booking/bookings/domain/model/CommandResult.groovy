package com.allaroundjava.booking.bookings.domain.model

import io.vavr.control.Either

class CommandResult {
    static boolean success(Either<? extends OccupationEvent, ? extends OccupationEvent> result) {
        return result.isRight()
    }

    static boolean failure(Either<? extends OccupationEvent, ? extends OccupationEvent> result) {
        return result.isLeft()
    }
}
