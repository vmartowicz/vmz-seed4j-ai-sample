package fr.vmz.seed4j.sample.ai.expert.gym.billing.infrastructure.secondary.account;

import fr.vmz.seed4j.sample.ai.expert.gym.billing.domain.BillingAccount;
import fr.vmz.seed4j.sample.ai.expert.gym.billing.domain.BillingAccountsRepository;
import fr.vmz.seed4j.sample.ai.shared.gym.domain.MemberId;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
class InMemoryBillingAccountsRepository implements BillingAccountsRepository {

  private final Map<MemberId, BillingAccount> accounts = new ConcurrentHashMap<>();

  @Override
  public void save(BillingAccount account) {
    accounts.put(account.member(), account);
  }

  @Override
  public Optional<BillingAccount> get(MemberId member) {
    return Optional.ofNullable(accounts.get(member));
  }

  @Override
  public List<BillingAccount> all() {
    return List.copyOf(accounts.values());
  }
}
