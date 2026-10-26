package fr.vmz.seed4j.sample.ai.expert.bank.accounts.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.bank.accounts.domain.AccountType;
import fr.vmz.seed4j.sample.ai.expert.bank.accounts.domain.BankAccount;
import fr.vmz.seed4j.sample.ai.expert.bank.accounts.domain.FreezeReason;
import fr.vmz.seed4j.sample.ai.shared.bank.domain.CustomerId;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;

record RestBankAccount(
  String iban,
  AccountType type,
  List<UUID> holders,
  BigDecimal authorizedOverdraft,
  BigDecimal balance,
  boolean unauthorizedOverdraft,
  boolean frozen,
  Set<FreezeReason> freezes,
  LocalDate openedOn,
  List<RestEntry> entries
) {
  static RestBankAccount from(BankAccount account) {
    return new RestBankAccount(
      account.iban().get(),
      account.type(),
      account.holders().stream().map(CustomerId::get).toList(),
      account.authorizedOverdraft(),
      account.balance(),
      account.isBeyondOverdraft(),
      account.isFrozen(),
      account.freezes(),
      account.openedOn(),
      account.entries().stream().map(RestEntry::from).toList()
    );
  }
}
