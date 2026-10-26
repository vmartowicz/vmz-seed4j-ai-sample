package fr.vmz.seed4j.sample.ai.expert.bank.accounts.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.bank.accounts.domain.CardAuthorization;
import fr.vmz.seed4j.sample.ai.expert.bank.accounts.domain.CardRefusal;
import org.jspecify.annotations.Nullable;

record RestCardAuthorization(boolean accepted, @Nullable CardRefusal refusal) {
  static RestCardAuthorization from(CardAuthorization authorization) {
    return new RestCardAuthorization(authorization.accepted(), authorization.refusal().orElse(null));
  }
}
