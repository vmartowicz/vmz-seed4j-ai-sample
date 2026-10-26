package fr.vmz.seed4j.sample.ai.expert.rental.reservation.infrastructure.secondary.loyalty;

import fr.vmz.seed4j.sample.ai.expert.rental.loyalty.infrastructure.primary.JavaLoyalty;
import fr.vmz.seed4j.sample.ai.expert.rental.reservation.domain.LoyaltyCard;
import fr.vmz.seed4j.sample.ai.shared.rental.domain.Email;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
class ReservationLoyaltyCard implements LoyaltyCard {

  private final JavaLoyalty loyalty;

  ReservationLoyaltyCard(JavaLoyalty loyalty) {
    this.loyalty = loyalty;
  }

  @Override
  public boolean isGold(Email customer) {
    return loyalty.isGold(customer.get());
  }

  @Override
  public boolean redeemFreeDay(Email customer, UUID reservation) {
    return loyalty.redeemFreeDay(customer.get(), reservation);
  }

  @Override
  public void cancelRedemption(Email customer, UUID reservation) {
    loyalty.cancelRedemption(customer.get(), reservation);
  }
}
