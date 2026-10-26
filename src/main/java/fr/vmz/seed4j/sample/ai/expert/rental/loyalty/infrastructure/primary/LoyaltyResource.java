package fr.vmz.seed4j.sample.ai.expert.rental.loyalty.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.rental.loyalty.application.LoyaltyApplicationService;
import fr.vmz.seed4j.sample.ai.shared.rental.domain.Email;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rental/loyalty")
class LoyaltyResource {

  private final LoyaltyApplicationService loyalty;

  LoyaltyResource(LoyaltyApplicationService loyalty) {
    this.loyalty = loyalty;
  }

  @GetMapping("/{customer}")
  RestLoyaltyAccount account(@PathVariable String customer) {
    return RestLoyaltyAccount.from(loyalty.account(new Email(customer)), loyalty.today());
  }
}
