package fr.vmz.seed4j.sample.ai.expert.bank.payments.domain;

import java.util.Optional;

public interface MandatesRepository {
  void save(Mandate mandate);

  Optional<Mandate> get(MandateId mandate);

  default Mandate mandateOf(MandateId mandate) {
    return get(mandate).orElseThrow(() -> new UnknownMandateException(mandate));
  }
}
