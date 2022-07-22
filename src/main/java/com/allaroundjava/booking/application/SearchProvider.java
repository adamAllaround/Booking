package com.allaroundjava.booking.application;

import com.allaroundjava.booking.bookings.domain.model.Interval;
import com.allaroundjava.booking.common.SearchItem;

import java.util.Set;

public interface SearchProvider {
    Set<SearchItem> search(Interval interval);
}
