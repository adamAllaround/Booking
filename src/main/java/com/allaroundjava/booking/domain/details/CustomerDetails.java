package com.allaroundjava.booking.domain.details;

import lombok.Value;

@Value
public class CustomerDetails {
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String phoneNumber;

}
