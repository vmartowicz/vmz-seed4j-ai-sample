package fr.vmz.seed4j.sample.ai.expert.bank.payments.infrastructure.secondary.mandate;

import fr.vmz.seed4j.sample.ai.expert.bank.payments.domain.DirectDebit;
import fr.vmz.seed4j.sample.ai.expert.bank.payments.domain.DirectDebitId;
import fr.vmz.seed4j.sample.ai.expert.bank.payments.domain.DirectDebitsRepository;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
class InMemoryDirectDebitsRepository implements DirectDebitsRepository {

  private final Map<DirectDebitId, DirectDebit> items = new ConcurrentHashMap<>();

  @Override
  public void save(DirectDebit item) {
    items.put(item.id(), item);
  }

  @Override
  public Optional<DirectDebit> get(DirectDebitId id) {
    return Optional.ofNullable(items.get(id));
  }
}
