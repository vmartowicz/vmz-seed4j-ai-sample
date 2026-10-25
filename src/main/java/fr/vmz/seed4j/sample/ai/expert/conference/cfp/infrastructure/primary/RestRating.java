package fr.vmz.seed4j.sample.ai.expert.conference.cfp.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.conference.cfp.domain.CommitteeMemberId;
import java.util.UUID;
import org.jspecify.annotations.Nullable;

record RestRating(@Nullable UUID memberId, int score) {
  CommitteeMemberId member() {
    return new CommitteeMemberId(memberId);
  }
}
