package fr.vmz.seed4j.sample.ai.expert.bank.payments.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.bank.payments.domain.DirectDebit;
import fr.vmz.seed4j.sample.ai.expert.bank.payments.domain.DirectDebitStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

record RestDirectDebit(UUID id, String account, String creditor, BigDecimal amount, LocalDateTime collectedAt, DirectDebitStatus status) {
  static RestDirectDebit from(DirectDebit debit) {
    return new RestDirectDebit(debit.id().get(), debit.account().get(), debit.creditor(), debit.amount(), debit.collectedAt(), debit.status());
  }
}
