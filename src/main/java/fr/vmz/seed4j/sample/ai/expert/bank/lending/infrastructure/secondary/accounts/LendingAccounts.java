package fr.vmz.seed4j.sample.ai.expert.bank.lending.infrastructure.secondary.accounts;

import fr.vmz.seed4j.sample.ai.expert.bank.accounts.infrastructure.primary.JavaAccounts;
import fr.vmz.seed4j.sample.ai.expert.bank.lending.domain.LoanAccounts;
import fr.vmz.seed4j.sample.ai.shared.bank.domain.CustomerId;
import fr.vmz.seed4j.sample.ai.shared.bank.domain.Iban;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
class LendingAccounts implements LoanAccounts {

  private final JavaAccounts accounts;

  LendingAccounts(JavaAccounts accounts) {
    this.accounts = accounts;
  }

  @Override
  public Optional<Iban> currentAccountOf(CustomerId customer) {
    return accounts.currentAccountOf(customer.get()).map(Iban::new);
  }

  @Override
  public boolean wasInUnauthorizedOverdraftSince(CustomerId customer, LocalDate date) {
    return accounts.wasInUnauthorizedOverdraftSince(customer.get(), date);
  }

  @Override
  public void credit(Iban account, BigDecimal amount, String label) {
    accounts.credit(account.get(), amount, label);
  }

  @Override
  public boolean collect(Iban account, BigDecimal amount, String label) {
    return accounts.collectForBank(account.get(), amount, label);
  }
}
