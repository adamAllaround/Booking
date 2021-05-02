package com.allaroundjava.booking.owners;

public interface MessageSender {
    void send(OwnerCreatedMessage ownerCreatedMessage);
}
