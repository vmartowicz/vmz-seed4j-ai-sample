package fr.vmz.seed4j.sample.ai.simple.bank.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.simple.bank.domain.Account;
import java.math.BigDecimal;
import java.util.UUID;

record RestAccount(UUID id, String holderName, BigDecimal balance) {
  static RestAccount from(Account account) {
    return new RestAccount(account.id().get(), account.holder().get(), account.balance().get());
  }
}
