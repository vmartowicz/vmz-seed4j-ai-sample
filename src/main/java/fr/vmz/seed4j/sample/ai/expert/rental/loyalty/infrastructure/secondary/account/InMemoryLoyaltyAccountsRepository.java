package fr.vmz.seed4j.sample.ai.expert.rental.loyalty.infrastructure.secondary.account;

import fr.vmz.seed4j.sample.ai.expert.rental.loyalty.domain.LoyaltyAccount;
import fr.vmz.seed4j.sample.ai.expert.rental.loyalty.domain.LoyaltyAccountsRepository;
import fr.vmz.seed4j.sample.ai.shared.rental.domain.Email;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
class InMemoryLoyaltyAccountsRepository implements LoyaltyAccountsRepository {

  private final Map<Email, LoyaltyAccount> accounts = new ConcurrentHashMap<>();

  @Override
  public void save(LoyaltyAccount account) {
    accounts.put(account.customer(), account);
  }

  @Override
  public Optional<LoyaltyAccount> get(Email customer) {
    return Optional.ofNullable(accounts.get(customer));
  }
}
