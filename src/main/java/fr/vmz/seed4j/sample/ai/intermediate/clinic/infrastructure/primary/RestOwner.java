package fr.vmz.seed4j.sample.ai.intermediate.clinic.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.intermediate.clinic.domain.owner.Owner;
import java.util.List;
import java.util.UUID;

record RestOwner(UUID id, String firstName, String lastName, String address, String city, String telephone, List<RestPet> pets) {
  static RestOwner from(Owner owner) {
    var information = owner.information();

    return new RestOwner(
      owner.id().get(),
      information.firstName().get(),
      information.lastName().get(),
      information.address().get(),
      information.city().get(),
      information.telephone().get(),
      owner.pets().stream().map(RestPet::from).toList()
    );
  }
}
