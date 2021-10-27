package com.allaroundjava.booking.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public abstract class Entity {
    private final UUID id;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Entity entity = (Entity) o;

        return id.equals(entity.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
