package fr.vmz.seed4j.sample.ai.advanced.leave.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.advanced.leave.domain.request.RejectionComment;
import java.util.Optional;
import org.jspecify.annotations.Nullable;

record RestRejection(@Nullable String comment) {
  Optional<RejectionComment> toDomain() {
    return Optional.ofNullable(comment).map(RejectionComment::new);
  }
}
