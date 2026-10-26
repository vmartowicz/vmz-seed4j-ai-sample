package fr.vmz.seed4j.sample.ai.expert.bank.compliance.infrastructure.secondary.sanction;

import fr.vmz.seed4j.sample.ai.expert.bank.compliance.domain.SanctionedName;
import fr.vmz.seed4j.sample.ai.expert.bank.compliance.domain.SanctionsRepository;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.springframework.stereotype.Repository;

@Repository
class InMemorySanctionsRepository implements SanctionsRepository {

  private final List<SanctionedName> sanctions = new CopyOnWriteArrayList<>();

  @Override
  public void save(SanctionedName name) {
    sanctions.add(name);
  }

  @Override
  public List<SanctionedName> all() {
    return List.copyOf(sanctions);
  }
}
