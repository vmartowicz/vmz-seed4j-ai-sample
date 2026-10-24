package fr.vmz.seed4j.sample.ai.clinic.domain.owner;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class Owner {

  private static final Comparator<Pet> BY_NAME = Comparator.comparing(pet -> pet.name().get(), String.CASE_INSENSITIVE_ORDER);

  private final OwnerId id;
  private final Map<PetId, Pet> pets = new LinkedHashMap<>();
  private OwnerInformation information;

  private Owner(OwnerId id, OwnerInformation information) {
    Assert.notNull("information", information);

    this.id = id;
    this.information = information;
  }

  public static Owner register(OwnerInformation information) {
    return new Owner(OwnerId.newId(), information);
  }

  public void update(OwnerInformation information) {
    Assert.notNull("information", information);

    this.information = information;
  }

  public Pet addPet(PetInformation pet, LocalDate today) {
    assertAcceptable(pet, today, pets.values());

    var added = new Pet(PetId.newId(), pet);
    pets.put(added.id(), added);

    return added;
  }

  public Pet updatePet(PetId petId, PetInformation information, LocalDate today) {
    var pet = pet(petId);
    assertAcceptable(information, today, otherPetsThan(pet));

    pet.update(information);

    return pet;
  }

  public Visit scheduleVisit(PetId petId, VisitToSchedule visit, LocalDate today) {
    Assert.notNull("visit", visit);
    Assert.notNull("today", today);

    var pet = pet(petId);
    if (!visit.date().isAfter(today)) {
      throw new VisitNotInFutureException(visit.date());
    }

    return pet.schedule(visit);
  }

  private void assertAcceptable(PetInformation pet, LocalDate today, Collection<Pet> otherPets) {
    Assert.notNull("pet", pet);
    Assert.notNull("today", today);

    if (pet.birthDate().isAfter(today)) {
      throw new PetBornInTheFutureException(pet.birthDate());
    }

    if (otherPets.stream().anyMatch(other -> other.name().sameAs(pet.name()))) {
      throw new DuplicatedPetNameException(pet.name());
    }
  }

  private List<Pet> otherPetsThan(Pet pet) {
    return pets
      .values()
      .stream()
      .filter(other -> !other.id().equals(pet.id()))
      .toList();
  }

  private Pet pet(PetId petId) {
    Assert.notNull("petId", petId);

    return Optional.ofNullable(pets.get(petId)).orElseThrow(() -> new UnknownPetException(petId));
  }

  public OwnerId id() {
    return id;
  }

  public OwnerInformation information() {
    return information;
  }

  public List<Pet> pets() {
    return pets.values().stream().sorted(BY_NAME).toList();
  }
}
