package fr.vmz.seed4j.sample.ai.expert.rental.billing.domain;

import fr.vmz.seed4j.sample.ai.shared.rental.domain.Email;

public interface LoyaltyPoints {
  void credit(Email customer, int points, String reference);
}
