package fr.vmz.seed4j.sample.ai.expert.bank.onboarding.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.bank.onboarding.application.OnboardingApplicationService;
import fr.vmz.seed4j.sample.ai.shared.bank.domain.CustomerId;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class JavaCustomers {

  private final OnboardingApplicationService onboarding;

  public JavaCustomers(OnboardingApplicationService onboarding) {
    this.onboarding = onboarding;
  }

  public Optional<JavaCustomer> customer(UUID customer) {
    var today = onboarding.today();

    return onboarding.find(new CustomerId(customer)).map(found -> JavaCustomer.from(found, today));
  }
}
