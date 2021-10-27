package com.allaroundjava.booking.bookings.adapters.api;

import com.allaroundjava.booking.bookings.adapters.db.OwnersDatabaseRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/owners")
@AllArgsConstructor
public class AddOwnerController {
    private final OwnersDatabaseRepository ownersRepository;

    @PostMapping
    ResponseEntity<OwnerResponse> addOwner(@RequestBody OwnerRequest ownerRequest) {
        OwnersDatabaseRepository.OwnerDatabaseEntity entity = ownersRepository.save(ownerRequest.toEntity());
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(entity.getId()).toUri();

        return ResponseEntity.created(uri)
                .body(OwnerResponse.from(entity));
    }
}

@Data
class OwnerRequest {
    String name;
    String email;

    OwnersDatabaseRepository.OwnerDatabaseEntity toEntity() {
        return OwnersDatabaseRepository.OwnerDatabaseEntity.now(name, email);
    }
}

@Value
class OwnerResponse {
    UUID id;
    String name;
    String contact;

    static OwnerResponse from(OwnersDatabaseRepository.OwnerDatabaseEntity owner) {
        return new OwnerResponse(owner.getId(), owner.getName(), owner.getEmail());
    }
}
