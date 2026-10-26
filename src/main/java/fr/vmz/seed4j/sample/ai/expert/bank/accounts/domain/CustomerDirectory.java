package fr.vmz.seed4j.sample.ai.expert.bank.accounts.domain;

import fr.vmz.seed4j.sample.ai.shared.bank.domain.CustomerId;
import java.util.Optional;

public interface CustomerDirectory {
  Optional<Holder> holder(CustomerId customer);
}
