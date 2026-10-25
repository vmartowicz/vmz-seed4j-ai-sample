package fr.vmz.seed4j.sample.ai.expert.gym.access.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.gym.access.domain.Entry;
import fr.vmz.seed4j.sample.ai.expert.gym.access.domain.RefusalReason;
import org.jspecify.annotations.Nullable;

record RestEntry(boolean accepted, @Nullable RefusalReason refusal, @Nullable String message) {
  static RestEntry from(Entry entry) {
    return new RestEntry(entry.accepted(), entry.refusal().orElse(null), entry.refusal().map(RefusalReason::message).orElse(null));
  }
}
