package fr.vmz.seed4j.sample.ai.simple.bank.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.simple.bank.domain.HolderName;
import org.jspecify.annotations.Nullable;

record RestAccountToOpen(@Nullable String holderName) {
  HolderName holder() {
    return new HolderName(holderName);
  }
}
