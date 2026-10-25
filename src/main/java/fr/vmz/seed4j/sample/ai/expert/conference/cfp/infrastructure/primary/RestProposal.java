package fr.vmz.seed4j.sample.ai.expert.conference.cfp.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.conference.cfp.domain.Format;
import fr.vmz.seed4j.sample.ai.expert.conference.cfp.domain.Level;
import fr.vmz.seed4j.sample.ai.expert.conference.cfp.domain.Proposal;
import fr.vmz.seed4j.sample.ai.expert.conference.cfp.domain.ProposalStatus;
import fr.vmz.seed4j.sample.ai.expert.conference.cfp.domain.Track;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.jspecify.annotations.Nullable;

record RestProposal(
  UUID id,
  String title,
  String summary,
  Format format,
  Level level,
  Track track,
  List<RestSpeaker> speakers,
  ProposalStatus status,
  int ratings,
  @Nullable BigDecimal average,
  @Nullable LocalDate confirmationDeadline,
  @Nullable UUID sponsorId
) {
  static RestProposal from(Proposal proposal) {
    return new RestProposal(
      proposal.id().get(),
      proposal.title(),
      proposal.summary(),
      proposal.format(),
      proposal.level(),
      proposal.track(),
      proposal.speakers().stream().map(RestSpeaker::from).toList(),
      proposal.status(),
      proposal.ratings(),
      proposal.average().orElse(null),
      proposal.confirmationDeadline().orElse(null),
      proposal.sponsor().orElse(null)
    );
  }
}
