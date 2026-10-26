package fr.vmz.seed4j.sample.ai.expert.bank.compliance.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.bank.compliance.domain.SanctionedName;
import org.jspecify.annotations.Nullable;

record RestSanction(@Nullable String name) {
  static RestSanction from(SanctionedName name) {
    return new RestSanction(name.get());
  }
}
