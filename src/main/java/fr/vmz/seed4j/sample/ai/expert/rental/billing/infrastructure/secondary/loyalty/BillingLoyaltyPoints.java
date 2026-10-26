package fr.vmz.seed4j.sample.ai.expert.rental.billing.infrastructure.secondary.loyalty;

import fr.vmz.seed4j.sample.ai.expert.rental.billing.domain.LoyaltyPoints;
import fr.vmz.seed4j.sample.ai.expert.rental.loyalty.infrastructure.primary.JavaLoyalty;
import fr.vmz.seed4j.sample.ai.shared.rental.domain.Email;
import org.springframework.stereotype.Component;

@Component
class BillingLoyaltyPoints implements LoyaltyPoints {

  private final JavaLoyalty loyalty;

  BillingLoyaltyPoints(JavaLoyalty loyalty) {
    this.loyalty = loyalty;
  }

  @Override
  public void credit(Email customer, int points, String reference) {
    loyalty.credit(customer.get(), points, reference);
  }
}
