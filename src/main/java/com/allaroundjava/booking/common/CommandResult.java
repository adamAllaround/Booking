package com.allaroundjava.booking.common;

import io.vavr.control.Either;

public class CommandResult {
    public static <L, R> Either<L, R> announceFailure(L left) {
        return Either.left(left);
    }

    public static <L, R> Either<L, R> announceSuccess(R right) {
        return Either.right(right);
    }
}
