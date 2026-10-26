package fr.vmz.seed4j.sample.ai.expert.bank.onboarding.domain;

import fr.vmz.seed4j.sample.ai.shared.bank.domain.CustomerId;

public interface AccountFreezes {
  void freezeAccountsOf(CustomerId customer);

  void unfreezeAccountsOf(CustomerId customer);
}
