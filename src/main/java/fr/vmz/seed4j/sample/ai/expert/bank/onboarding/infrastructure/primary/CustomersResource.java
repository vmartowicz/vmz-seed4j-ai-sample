package fr.vmz.seed4j.sample.ai.expert.bank.onboarding.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.bank.onboarding.application.OnboardingApplicationService;
import fr.vmz.seed4j.sample.ai.shared.bank.domain.CustomerId;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/retail-bank/customers")
class CustomersResource {

  private final OnboardingApplicationService onboarding;

  CustomersResource(OnboardingApplicationService onboarding) {
    this.onboarding = onboarding;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  RestCustomer onboard(@RequestBody RestProspectToOnboard prospect) {
    return RestCustomer.from(onboarding.onboard(prospect.toDomain()), onboarding.today());
  }

  @GetMapping("/{customerId}")
  RestCustomer customer(@PathVariable UUID customerId) {
    return RestCustomer.from(onboarding.customer(new CustomerId(customerId)), onboarding.today());
  }

  @PostMapping("/{customerId}/review")
  RestCustomer review(@PathVariable UUID customerId, @RequestBody RestReview review) {
    return RestCustomer.from(onboarding.review(new CustomerId(customerId), review.risk()), onboarding.today());
  }
}
