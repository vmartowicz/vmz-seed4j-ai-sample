package fr.vmz.seed4j.sample.ai.expert.bank.accounts.infrastructure.secondary.onboarding;

import fr.vmz.seed4j.sample.ai.expert.bank.accounts.domain.CustomerDirectory;
import fr.vmz.seed4j.sample.ai.expert.bank.accounts.domain.Holder;
import fr.vmz.seed4j.sample.ai.expert.bank.onboarding.infrastructure.primary.JavaCustomers;
import fr.vmz.seed4j.sample.ai.shared.bank.domain.CustomerId;
import java.util.Optional;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
class AccountsCustomerDirectory implements CustomerDirectory {

  private final JavaCustomers customers;

  // Lazy: onboarding freezes accounts, the contexts depend on each other
  AccountsCustomerDirectory(@Lazy JavaCustomers customers) {
    this.customers = customers;
  }

  @Override
  public Optional<Holder> holder(CustomerId customer) {
    return customers
      .customer(customer.get())
      .map(found -> new Holder(customer, found.birthDate(), found.monthlyIncome(), found.minor()));
  }
}
