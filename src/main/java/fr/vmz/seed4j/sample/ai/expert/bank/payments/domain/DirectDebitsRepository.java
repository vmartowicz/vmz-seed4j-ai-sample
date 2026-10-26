package fr.vmz.seed4j.sample.ai.expert.bank.payments.domain;

import java.util.Optional;

public interface DirectDebitsRepository {
  void save(DirectDebit debit);

  Optional<DirectDebit> get(DirectDebitId debit);

  default DirectDebit debitOf(DirectDebitId debit) {
    return get(debit).orElseThrow(() -> new UnknownDirectDebitException(debit));
  }
}
