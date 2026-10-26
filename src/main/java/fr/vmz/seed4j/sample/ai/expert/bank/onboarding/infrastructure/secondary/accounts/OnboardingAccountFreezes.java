package fr.vmz.seed4j.sample.ai.expert.bank.onboarding.infrastructure.secondary.accounts;

import fr.vmz.seed4j.sample.ai.expert.bank.accounts.infrastructure.primary.JavaAccounts;
import fr.vmz.seed4j.sample.ai.expert.bank.onboarding.domain.AccountFreezes;
import fr.vmz.seed4j.sample.ai.shared.bank.domain.CustomerId;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
class OnboardingAccountFreezes implements AccountFreezes {

  private final JavaAccounts accounts;

  // Lazy: accounts read customers back, the contexts depend on each other
  OnboardingAccountFreezes(@Lazy JavaAccounts accounts) {
    this.accounts = accounts;
  }

  @Override
  public void freezeAccountsOf(CustomerId customer) {
    accounts.freezeAccountsOf(customer.get());
  }

  @Override
  public void unfreezeAccountsOf(CustomerId customer) {
    accounts.unfreezeAccountsOf(customer.get());
  }
}
