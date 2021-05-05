package com.allaroundjava.booking.owners;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/owners")
@AllArgsConstructor
class OwnersController {
    private final OwnersService ownersService;

    @GetMapping
    ResponseEntity<OwnersResponse> listOwners() {
        Collection<Owner> owners = ownersService.getAll();
        return ResponseEntity.ok(OwnersResponse.from(owners));
    }

    @GetMapping(path = "/{id}")
    ResponseEntity<OwnerResponse> singleOwner(@PathVariable UUID id) {
        return ownersService.getSingle(id)
                .map(OwnerResponse::from)
                .map(owner -> ResponseEntity.ok().body(owner))
                .orElse(ResponseEntity.badRequest().build());
    }

    @PostMapping
    ResponseEntity<OwnerResponse> addOwner(@RequestBody OwnerRequest ownerRequest) {
        Owner owner = ownersService.save(ownerRequest.toModel());
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(owner.getId()).toUri();

        return ResponseEntity.created(uri)
                .body(OwnerResponse.from(owner));
    }

    @Value
    static class OwnersResponse {
        Collection<OwnerResponse> owners;

        static OwnersResponse from(Collection<Owner> owners) {
            List<OwnerResponse> collection = owners.stream()
                    .map(OwnerResponse::from)
                    .collect(Collectors.toUnmodifiableList());
            return new OwnersResponse(collection);
        }
    }

    @Value
    static class OwnerResponse {
        UUID id;
        String name;
        String contact;

        static OwnerResponse from(Owner owner) {
            return new OwnerResponse(owner.getId(), owner.getName(), owner.getContact());
        }
    }

    @Data
    static class OwnerRequest {
        String name;
        String contact;

        Owner toModel() {
            Owner owner = new Owner();
            owner.setName(name);
            owner.setContact(contact);
            return owner;
        }
    }
}



