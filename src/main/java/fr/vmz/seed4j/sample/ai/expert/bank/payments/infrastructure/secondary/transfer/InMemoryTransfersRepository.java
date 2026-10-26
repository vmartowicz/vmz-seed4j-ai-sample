package fr.vmz.seed4j.sample.ai.expert.bank.payments.infrastructure.secondary.transfer;

import fr.vmz.seed4j.sample.ai.expert.bank.payments.domain.Transfer;
import fr.vmz.seed4j.sample.ai.expert.bank.payments.domain.TransferId;
import fr.vmz.seed4j.sample.ai.expert.bank.payments.domain.TransfersRepository;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
class InMemoryTransfersRepository implements TransfersRepository {

  private final Map<TransferId, Transfer> items = new ConcurrentHashMap<>();

  @Override
  public void save(Transfer item) {
    items.put(item.id(), item);
  }

  @Override
  public Optional<Transfer> get(TransferId id) {
    return Optional.ofNullable(items.get(id));
  }

  @Override
  public List<Transfer> all() {
    return List.copyOf(items.values());
  }
}
