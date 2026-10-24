package fr.vmz.seed4j.sample.ai.intermediate.clinic.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.intermediate.clinic.application.OwnersApplicationService;
import fr.vmz.seed4j.sample.ai.intermediate.clinic.domain.Pagination;
import fr.vmz.seed4j.sample.ai.intermediate.clinic.domain.owner.OwnerId;
import fr.vmz.seed4j.sample.ai.intermediate.clinic.domain.owner.OwnersSearch;
import fr.vmz.seed4j.sample.ai.intermediate.clinic.domain.owner.PetId;
import java.util.Objects;
import java.util.UUID;
import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/owners")
class OwnersResource {

  private final OwnersApplicationService owners;

  OwnersResource(OwnersApplicationService owners) {
    this.owners = owners;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  RestOwner register(@RequestBody RestOwnerInformation owner) {
    return RestOwner.from(owners.register(owner.toDomain()));
  }

  @PutMapping("/{ownerId}")
  RestOwner update(@PathVariable UUID ownerId, @RequestBody RestOwnerInformation owner) {
    return RestOwner.from(owners.update(new OwnerId(ownerId), owner.toDomain()));
  }

  @GetMapping
  RestClinicPage<RestOwner> search(
    @RequestParam(required = false) @Nullable String lastName,
    @RequestParam(defaultValue = "1") int page
  ) {
    var search = new OwnersSearch(Objects.requireNonNullElse(lastName, ""), new Pagination(page));

    return RestClinicPage.from(owners.search(search), RestOwner::from);
  }

  @GetMapping("/{ownerId}")
  RestOwner get(@PathVariable UUID ownerId) {
    return RestOwner.from(owners.get(new OwnerId(ownerId)));
  }

  @PostMapping("/{ownerId}/pets")
  @ResponseStatus(HttpStatus.CREATED)
  RestPet addPet(@PathVariable UUID ownerId, @RequestBody RestPetInformation pet) {
    return RestPet.from(owners.addPet(new OwnerId(ownerId), pet.toDomain()));
  }

  @PutMapping("/{ownerId}/pets/{petId}")
  RestPet updatePet(@PathVariable UUID ownerId, @PathVariable UUID petId, @RequestBody RestPetInformation pet) {
    return RestPet.from(owners.updatePet(new OwnerId(ownerId), new PetId(petId), pet.toDomain()));
  }

  @PostMapping("/{ownerId}/pets/{petId}/visits")
  @ResponseStatus(HttpStatus.CREATED)
  RestVisit scheduleVisit(@PathVariable UUID ownerId, @PathVariable UUID petId, @RequestBody RestVisitToSchedule visit) {
    return RestVisit.from(owners.scheduleVisit(new OwnerId(ownerId), new PetId(petId), visit.toDomain()));
  }
}
