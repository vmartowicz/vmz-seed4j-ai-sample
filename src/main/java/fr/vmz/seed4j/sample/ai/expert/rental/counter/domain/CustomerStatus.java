package fr.vmz.seed4j.sample.ai.expert.rental.counter.domain;

import fr.vmz.seed4j.sample.ai.shared.rental.domain.Email;

public interface CustomerStatus {
  boolean isGold(Email customer);
}
