package fr.vmz.seed4j.sample.ai.expert.rental.billing.infrastructure.secondary.deposit;

import fr.vmz.seed4j.sample.ai.expert.rental.billing.domain.Deposit;
import fr.vmz.seed4j.sample.ai.expert.rental.billing.domain.DepositsRepository;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
class InMemoryDepositsRepository implements DepositsRepository {

  private final Map<UUID, Deposit> deposits = new ConcurrentHashMap<>();

  @Override
  public void save(Deposit deposit) {
    deposits.put(deposit.contract(), deposit);
  }

  @Override
  public Optional<Deposit> get(UUID contract) {
    return Optional.ofNullable(deposits.get(contract));
  }

  @Override
  public List<Deposit> all() {
    return List.copyOf(deposits.values());
  }
}
