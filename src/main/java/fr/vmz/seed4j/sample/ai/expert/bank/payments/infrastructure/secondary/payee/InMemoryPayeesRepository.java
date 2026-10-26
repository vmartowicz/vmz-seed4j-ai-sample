package fr.vmz.seed4j.sample.ai.expert.bank.payments.infrastructure.secondary.payee;

import fr.vmz.seed4j.sample.ai.expert.bank.payments.domain.Payee;
import fr.vmz.seed4j.sample.ai.expert.bank.payments.domain.PayeeId;
import fr.vmz.seed4j.sample.ai.expert.bank.payments.domain.PayeesRepository;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
class InMemoryPayeesRepository implements PayeesRepository {

  private final Map<PayeeId, Payee> items = new ConcurrentHashMap<>();

  @Override
  public void save(Payee item) {
    items.put(item.id(), item);
  }

  @Override
  public Optional<Payee> get(PayeeId id) {
    return Optional.ofNullable(items.get(id));
  }
}
