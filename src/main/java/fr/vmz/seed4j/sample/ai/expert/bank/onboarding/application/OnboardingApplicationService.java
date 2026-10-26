package fr.vmz.seed4j.sample.ai.expert.bank.onboarding.application;

import fr.vmz.seed4j.sample.ai.expert.bank.onboarding.domain.AccountFreezes;
import fr.vmz.seed4j.sample.ai.expert.bank.onboarding.domain.Customer;
import fr.vmz.seed4j.sample.ai.expert.bank.onboarding.domain.CustomersRepository;
import fr.vmz.seed4j.sample.ai.expert.bank.onboarding.domain.Onboarding;
import fr.vmz.seed4j.sample.ai.expert.bank.onboarding.domain.ProspectToOnboard;
import fr.vmz.seed4j.sample.ai.expert.bank.onboarding.domain.RiskLevel;
import fr.vmz.seed4j.sample.ai.shared.bank.domain.CustomerId;
import java.time.Clock;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class OnboardingApplicationService {

  private final CustomersRepository customers;
  private final Onboarding onboarding;
  private final Clock clock;

  public OnboardingApplicationService(CustomersRepository customers, AccountFreezes freezes, Clock clock) {
    this.customers = customers;
    onboarding = new Onboarding(customers, freezes);
    this.clock = clock;
  }

  public Customer onboard(ProspectToOnboard prospect) {
    return onboarding.onboard(prospect, today());
  }

  public Customer review(CustomerId customer, RiskLevel risk) {
    return onboarding.review(customer, risk, today());
  }

  public void freezeOverdueReviews() {
    onboarding.freezeOverdueReviews(today());
  }

  public Customer customer(CustomerId customer) {
    return onboarding.customer(customer);
  }

  public Optional<Customer> find(CustomerId customer) {
    return customers.get(customer);
  }

  public LocalDate today() {
    return LocalDate.now(clock);
  }
}
