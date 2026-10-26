package fr.vmz.seed4j.sample.ai.expert.bank.payments.domain;

import java.util.Optional;

public interface PayeesRepository {
  void save(Payee payee);

  Optional<Payee> get(PayeeId payee);
}
