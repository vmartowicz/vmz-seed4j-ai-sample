package fr.vmz.seed4j.sample.ai.expert.bank.accounts.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.bank.accounts.domain.AccountType;
import fr.vmz.seed4j.sample.ai.shared.bank.domain.CustomerId;
import java.util.List;
import java.util.UUID;
import org.jspecify.annotations.Nullable;

record RestAccountToOpen(@Nullable AccountType type, List<UUID> holders) {
  List<CustomerId> holderIds() {
    return holders.stream().map(CustomerId::new).toList();
  }
}
