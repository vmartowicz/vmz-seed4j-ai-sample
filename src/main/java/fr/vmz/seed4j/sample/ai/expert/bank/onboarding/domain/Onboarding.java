package fr.vmz.seed4j.sample.ai.expert.bank.onboarding.domain;

import fr.vmz.seed4j.sample.ai.shared.bank.domain.CustomerId;
import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.time.LocalDate;

public class Onboarding {

  private final CustomersRepository customers;
  private final AccountFreezes freezes;

  public Onboarding(CustomersRepository customers, AccountFreezes freezes) {
    this.customers = customers;
    this.freezes = freezes;
  }

  public Customer onboard(ProspectToOnboard prospect, LocalDate today) {
    Assert.notNull("prospect", prospect);

    var customer = prospect.onboard(today);
    customers.save(customer);

    return customer;
  }

  public Customer review(CustomerId id, RiskLevel risk, LocalDate today) {
    Assert.notNull("risk", risk);
    var customer = customers.customer(id);

    customer.review(risk, today);
    customers.save(customer);
    freezes.unfreezeAccountsOf(customer.id());

    return customer;
  }

  public void freezeOverdueReviews(LocalDate today) {
    customers
      .all()
      .stream()
      .filter(customer -> customer.isReviewOverdueOn(today))
      .forEach(customer -> freezes.freezeAccountsOf(customer.id()));
  }

  public Customer customer(CustomerId id) {
    return customers.customer(id);
  }
}
