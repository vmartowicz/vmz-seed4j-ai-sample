package fr.vmz.seed4j.sample.ai.expert.bank.accounts.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.bank.accounts.domain.Entry;
import java.math.BigDecimal;
import java.time.LocalDateTime;

record RestEntry(LocalDateTime at, BigDecimal amount, String label) {
  static RestEntry from(Entry entry) {
    return new RestEntry(entry.at(), entry.amount(), entry.label());
  }
}
