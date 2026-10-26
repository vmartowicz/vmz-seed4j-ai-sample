package fr.vmz.seed4j.sample.ai.expert.rental.loyalty.domain;

import fr.vmz.seed4j.sample.ai.shared.rental.domain.Email;
import java.util.Optional;

public interface LoyaltyAccountsRepository {
  void save(LoyaltyAccount account);

  Optional<LoyaltyAccount> get(Email customer);
}
