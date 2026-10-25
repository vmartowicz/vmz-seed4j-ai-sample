package fr.vmz.seed4j.sample.ai.expert.conference.cfp.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.conference.cfp.domain.Format;
import fr.vmz.seed4j.sample.ai.expert.conference.cfp.domain.Level;
import fr.vmz.seed4j.sample.ai.expert.conference.cfp.domain.ProposalToSubmit;
import fr.vmz.seed4j.sample.ai.expert.conference.cfp.domain.Track;
import java.util.List;
import java.util.UUID;
import org.jspecify.annotations.Nullable;

record RestProposalToSubmit(
  @Nullable UUID sponsorId,
  @Nullable String title,
  @Nullable String summary,
  @Nullable Format format,
  @Nullable Level level,
  @Nullable Track track,
  List<RestSpeaker> speakers
) {
  ProposalToSubmit toDomain() {
    return new ProposalToSubmit(title, summary, format, level, track, speakers.stream().map(RestSpeaker::toDomain).toList());
  }
}
