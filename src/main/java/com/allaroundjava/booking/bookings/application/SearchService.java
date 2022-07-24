package com.allaroundjava.booking.bookings.application;

import com.allaroundjava.booking.bookings.domain.model.Interval;
import com.allaroundjava.booking.common.SearchItem;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

class SearchService {
    private final Collection<SearchProvider> searchProviders;

    Set<SearchItem> search(Interval interval) {
        Set<Set<SearchItem>> collect = searchProviders.stream().map(provider -> provider.search(interval)).collect(Collectors.toSet());

    }
}
