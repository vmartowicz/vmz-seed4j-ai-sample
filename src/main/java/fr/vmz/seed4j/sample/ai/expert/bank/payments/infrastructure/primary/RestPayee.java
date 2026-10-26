package fr.vmz.seed4j.sample.ai.expert.bank.payments.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.bank.payments.domain.Payee;
import java.time.LocalDateTime;
import java.util.UUID;

record RestPayee(UUID id, String payer, String name, String iban, LocalDateTime addedAt) {
  static RestPayee from(Payee payee) {
    return new RestPayee(payee.id().get(), payee.payer().get(), payee.name(), payee.iban().get(), payee.addedAt());
  }
}
