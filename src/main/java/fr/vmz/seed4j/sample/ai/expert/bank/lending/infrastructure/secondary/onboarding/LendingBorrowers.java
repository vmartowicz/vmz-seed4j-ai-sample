package fr.vmz.seed4j.sample.ai.expert.bank.lending.infrastructure.secondary.onboarding;

import fr.vmz.seed4j.sample.ai.expert.bank.lending.domain.Borrower;
import fr.vmz.seed4j.sample.ai.expert.bank.lending.domain.Borrowers;
import fr.vmz.seed4j.sample.ai.expert.bank.onboarding.infrastructure.primary.JavaCustomers;
import fr.vmz.seed4j.sample.ai.shared.bank.domain.CustomerId;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
class LendingBorrowers implements Borrowers {

  private final JavaCustomers customers;

  LendingBorrowers(JavaCustomers customers) {
    this.customers = customers;
  }

  @Override
  public Optional<Borrower> borrower(CustomerId customer) {
    return customers
      .customer(customer.get())
      .map(found -> new Borrower(customer, found.birthDate(), found.monthlyIncome(), found.customerSince()));
  }
}
