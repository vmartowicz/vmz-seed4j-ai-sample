package fr.vmz.seed4j.sample.ai.intermediate.clinic.application;

import fr.vmz.seed4j.sample.ai.intermediate.clinic.domain.ClinicPage;
import fr.vmz.seed4j.sample.ai.intermediate.clinic.domain.owner.Owner;
import fr.vmz.seed4j.sample.ai.intermediate.clinic.domain.owner.OwnerId;
import fr.vmz.seed4j.sample.ai.intermediate.clinic.domain.owner.OwnerInformation;
import fr.vmz.seed4j.sample.ai.intermediate.clinic.domain.owner.OwnersRepository;
import fr.vmz.seed4j.sample.ai.intermediate.clinic.domain.owner.OwnersSearch;
import fr.vmz.seed4j.sample.ai.intermediate.clinic.domain.owner.Pet;
import fr.vmz.seed4j.sample.ai.intermediate.clinic.domain.owner.PetId;
import fr.vmz.seed4j.sample.ai.intermediate.clinic.domain.owner.PetInformation;
import fr.vmz.seed4j.sample.ai.intermediate.clinic.domain.owner.UnknownOwnerException;
import fr.vmz.seed4j.sample.ai.intermediate.clinic.domain.owner.Visit;
import fr.vmz.seed4j.sample.ai.intermediate.clinic.domain.owner.VisitToSchedule;
import java.time.Clock;
import java.time.LocalDate;
import org.springframework.stereotype.Service;

@Service
public class OwnersApplicationService {

  private final OwnersRepository owners;
  private final Clock clock;

  public OwnersApplicationService(OwnersRepository owners, Clock clock) {
    this.owners = owners;
    this.clock = clock;
  }

  public Owner register(OwnerInformation information) {
    var owner = Owner.register(information);
    owners.save(owner);

    return owner;
  }

  public Owner update(OwnerId ownerId, OwnerInformation information) {
    var owner = get(ownerId);
    owner.update(information);
    owners.save(owner);

    return owner;
  }

  public ClinicPage<Owner> search(OwnersSearch search) {
    return owners.search(search);
  }

  public Owner get(OwnerId ownerId) {
    return owners.get(ownerId).orElseThrow(() -> new UnknownOwnerException(ownerId));
  }

  public Pet addPet(OwnerId ownerId, PetInformation pet) {
    var owner = get(ownerId);
    var added = owner.addPet(pet, today());
    owners.save(owner);

    return added;
  }

  public Pet updatePet(OwnerId ownerId, PetId petId, PetInformation pet) {
    var owner = get(ownerId);
    var updated = owner.updatePet(petId, pet, today());
    owners.save(owner);

    return updated;
  }

  public Visit scheduleVisit(OwnerId ownerId, PetId petId, VisitToSchedule visit) {
    var owner = get(ownerId);
    var scheduled = owner.scheduleVisit(petId, visit, today());
    owners.save(owner);

    return scheduled;
  }

  private LocalDate today() {
    return LocalDate.now(clock);
  }
}
