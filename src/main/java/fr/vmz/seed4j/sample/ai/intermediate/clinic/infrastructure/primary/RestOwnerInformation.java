package fr.vmz.seed4j.sample.ai.intermediate.clinic.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.intermediate.clinic.domain.FirstName;
import fr.vmz.seed4j.sample.ai.intermediate.clinic.domain.LastName;
import fr.vmz.seed4j.sample.ai.intermediate.clinic.domain.owner.Address;
import fr.vmz.seed4j.sample.ai.intermediate.clinic.domain.owner.City;
import fr.vmz.seed4j.sample.ai.intermediate.clinic.domain.owner.OwnerInformation;
import fr.vmz.seed4j.sample.ai.intermediate.clinic.domain.owner.Telephone;
import org.jspecify.annotations.Nullable;

record RestOwnerInformation(
  @Nullable String firstName,
  @Nullable String lastName,
  @Nullable String address,
  @Nullable String city,
  @Nullable String telephone
) {
  OwnerInformation toDomain() {
    return new OwnerInformation(
      new FirstName(firstName),
      new LastName(lastName),
      new Address(address),
      new City(city),
      new Telephone(telephone)
    );
  }
}
