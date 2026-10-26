package fr.vmz.seed4j.sample.ai.expert.bank.payments.infrastructure.secondary.mandate;

import fr.vmz.seed4j.sample.ai.expert.bank.payments.domain.Mandate;
import fr.vmz.seed4j.sample.ai.expert.bank.payments.domain.MandateId;
import fr.vmz.seed4j.sample.ai.expert.bank.payments.domain.MandatesRepository;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
class InMemoryMandatesRepository implements MandatesRepository {

  private final Map<MandateId, Mandate> items = new ConcurrentHashMap<>();

  @Override
  public void save(Mandate item) {
    items.put(item.id(), item);
  }

  @Override
  public Optional<Mandate> get(MandateId id) {
    return Optional.ofNullable(items.get(id));
  }
}
