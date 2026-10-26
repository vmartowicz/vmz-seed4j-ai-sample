package fr.vmz.seed4j.sample.ai.expert.rental.loyalty.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.rental.loyalty.application.LoyaltyApplicationService;
import fr.vmz.seed4j.sample.ai.expert.rental.loyalty.domain.LoyaltyStatus;
import fr.vmz.seed4j.sample.ai.expert.rental.loyalty.domain.NotEnoughPointsException;
import fr.vmz.seed4j.sample.ai.shared.rental.domain.Email;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class JavaLoyalty {

  private final LoyaltyApplicationService loyalty;

  public JavaLoyalty(LoyaltyApplicationService loyalty) {
    this.loyalty = loyalty;
  }

  public boolean isGold(String customer) {
    return loyalty.statusOf(new Email(customer)) == LoyaltyStatus.GOLD;
  }

  public boolean redeemFreeDay(String customer, UUID reservation) {
    try {
      loyalty.redeemFreeDay(new Email(customer), reservation);
      return true;
    } catch (NotEnoughPointsException _) {
      return false;
    }
  }

  public void cancelRedemption(String customer, UUID reservation) {
    loyalty.cancelRedemption(new Email(customer), reservation);
  }

  public void credit(String customer, int points, String reference) {
    loyalty.credit(new Email(customer), points, reference);
  }
}
