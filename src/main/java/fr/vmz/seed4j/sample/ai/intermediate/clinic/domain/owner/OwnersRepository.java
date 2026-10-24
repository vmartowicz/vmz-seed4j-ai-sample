package fr.vmz.seed4j.sample.ai.intermediate.clinic.domain.owner;

import fr.vmz.seed4j.sample.ai.intermediate.clinic.domain.ClinicPage;
import java.util.Optional;

public interface OwnersRepository {
  void save(Owner owner);

  Optional<Owner> get(OwnerId owner);

  ClinicPage<Owner> search(OwnersSearch search);
}
