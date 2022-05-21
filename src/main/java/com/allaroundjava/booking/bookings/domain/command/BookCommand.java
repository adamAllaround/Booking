package com.allaroundjava.booking.bookings.domain.command;

import com.allaroundjava.booking.bookings.domain.model.Customer;
import lombok.Value;

import java.util.UUID;

@Value
public class BookCommand {
    UUID basketId;
    Customer customer;
}
