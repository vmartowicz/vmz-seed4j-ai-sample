package fr.vmz.seed4j.sample.ai.expert.gym.billing.domain;

import fr.vmz.seed4j.sample.ai.shared.gym.domain.MemberId;
import java.util.List;
import java.util.Optional;

public interface BillingAccountsRepository {
  void save(BillingAccount account);

  Optional<BillingAccount> get(MemberId member);

  List<BillingAccount> all();

  default BillingAccount account(MemberId member) {
    return get(member).orElseThrow(() -> new UnknownBillingAccountException(member));
  }
}
