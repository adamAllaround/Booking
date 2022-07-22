package com.allaroundjava.booking.common;

import java.util.UUID;

public class SearchItem {
    UUID roomId;
    Money price;
    int capacity;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SearchItem that = (SearchItem) o;

        return roomId.equals(that.roomId);
    }

    @Override
    public int hashCode() {
        return roomId.hashCode();
    }
}
