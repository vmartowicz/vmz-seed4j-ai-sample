package fr.vmz.seed4j.sample.ai.expert.rental.counter.infrastructure.secondary.loyalty;

import fr.vmz.seed4j.sample.ai.expert.rental.counter.domain.CustomerStatus;
import fr.vmz.seed4j.sample.ai.expert.rental.loyalty.infrastructure.primary.JavaLoyalty;
import fr.vmz.seed4j.sample.ai.shared.rental.domain.Email;
import org.springframework.stereotype.Component;

@Component
class CounterCustomerStatus implements CustomerStatus {

  private final JavaLoyalty loyalty;

  CounterCustomerStatus(JavaLoyalty loyalty) {
    this.loyalty = loyalty;
  }

  @Override
  public boolean isGold(Email customer) {
    return loyalty.isGold(customer.get());
  }
}
