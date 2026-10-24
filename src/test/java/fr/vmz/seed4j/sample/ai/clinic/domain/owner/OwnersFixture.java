package fr.vmz.seed4j.sample.ai.clinic.domain.owner;

import fr.vmz.seed4j.sample.ai.clinic.domain.FirstName;
import fr.vmz.seed4j.sample.ai.clinic.domain.LastName;
import java.time.LocalDate;

public final class OwnersFixture {

  public static final LocalDate TODAY = LocalDate.of(2026, 3, 10);

  private OwnersFixture() {}

  public static Owner owner() {
    return Owner.register(ownerInformation());
  }

  public static OwnerInformation ownerInformation() {
    return ownerInformation("George", "Franklin");
  }

  public static OwnerInformation ownerInformation(String firstName, String lastName) {
    return new OwnerInformation(
      new FirstName(firstName),
      new LastName(lastName),
      new Address("110 W. Liberty St."),
      new City("Madison"),
      new Telephone("6085551023")
    );
  }

  public static PetInformation leo() {
    return pet("Leo");
  }

  public static PetInformation pet(String name) {
    return new PetInformation(new PetName(name), LocalDate.of(2020, 9, 7), PetType.CAT);
  }

  public static VisitToSchedule rabiesShot() {
    return visitOn(TODAY.plusDays(1));
  }

  public static VisitToSchedule visitOn(LocalDate date) {
    return new VisitToSchedule(date, new VisitDescription("Rabies shot"));
  }
}
