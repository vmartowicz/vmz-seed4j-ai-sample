package fr.vmz.seed4j.sample.ai.intermediate.clinic.infrastructure.secondary.owner;

import fr.vmz.seed4j.sample.ai.intermediate.clinic.domain.ClinicPage;
import fr.vmz.seed4j.sample.ai.intermediate.clinic.domain.owner.Owner;
import fr.vmz.seed4j.sample.ai.intermediate.clinic.domain.owner.OwnerId;
import fr.vmz.seed4j.sample.ai.intermediate.clinic.domain.owner.OwnersRepository;
import fr.vmz.seed4j.sample.ai.intermediate.clinic.domain.owner.OwnersSearch;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
class InMemoryOwnersRepository implements OwnersRepository {

  private final Map<OwnerId, Owner> owners = new ConcurrentHashMap<>();

  @Override
  public void save(Owner owner) {
    owners.put(owner.id(), owner);
  }

  @Override
  public Optional<Owner> get(OwnerId owner) {
    return Optional.ofNullable(owners.get(owner));
  }

  @Override
  public ClinicPage<Owner> search(OwnersSearch search) {
    var matchingOwners = owners.values().stream().filter(search::matches).sorted(OwnersSearch.ORDER).toList();

    return ClinicPage.of(matchingOwners, search.pagination());
  }
}
