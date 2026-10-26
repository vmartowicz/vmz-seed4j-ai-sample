package fr.vmz.seed4j.sample.ai.expert.rental.billing.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DepositsRepository {
  void save(Deposit deposit);

  Optional<Deposit> get(UUID contract);

  List<Deposit> all();

  default Deposit deposit(UUID contract) {
    return get(contract).orElseThrow(() -> new UnknownDepositException(contract));
  }
}
