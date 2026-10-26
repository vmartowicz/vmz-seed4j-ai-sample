package fr.vmz.seed4j.sample.ai.expert.rental.reservation.domain;

import fr.vmz.seed4j.sample.ai.shared.rental.domain.Email;
import java.util.UUID;

public interface LoyaltyCard {
  boolean isGold(Email customer);

  boolean redeemFreeDay(Email customer, UUID reservation);

  void cancelRedemption(Email customer, UUID reservation);
}
