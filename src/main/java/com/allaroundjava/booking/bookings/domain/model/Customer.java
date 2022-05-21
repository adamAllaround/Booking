package com.allaroundjava.booking.bookings.domain.model;

import com.allaroundjava.booking.common.ValueObject;
import lombok.Value;

@Value
public class Customer extends ValueObject {
    String firstName;
    String lastName;
    String email;
    String phone;
}
