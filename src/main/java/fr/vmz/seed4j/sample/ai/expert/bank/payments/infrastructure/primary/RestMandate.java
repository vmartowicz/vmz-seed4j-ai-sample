package fr.vmz.seed4j.sample.ai.expert.bank.payments.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.bank.payments.domain.Mandate;
import java.time.LocalDate;
import java.util.UUID;

record RestMandate(UUID id, String account, String creditor, LocalDate signedOn, boolean active) {
  static RestMandate from(Mandate mandate) {
    return new RestMandate(mandate.id().get(), mandate.account().get(), mandate.creditor(), mandate.signedOn(), mandate.isActive());
  }
}
