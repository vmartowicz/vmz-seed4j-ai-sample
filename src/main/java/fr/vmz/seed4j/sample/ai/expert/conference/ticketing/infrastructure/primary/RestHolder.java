package fr.vmz.seed4j.sample.ai.expert.conference.ticketing.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.conference.ticketing.domain.Holder;
import fr.vmz.seed4j.sample.ai.shared.conference.domain.Email;
import org.jspecify.annotations.Nullable;

record RestHolder(@Nullable String name, @Nullable String email) {
  Holder toDomain() {
    return new Holder(name, new Email(email));
  }
}
