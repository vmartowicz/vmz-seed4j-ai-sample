package fr.vmz.seed4j.sample.ai.expert.conference.cfp.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.conference.cfp.domain.Proposal;
import fr.vmz.seed4j.sample.ai.expert.conference.cfp.domain.SpeakerProfile;
import java.util.List;
import java.util.UUID;

public record JavaConfirmedTalk(UUID id, String title, String format, String track, List<String> speakers, boolean speakerFromAbroad) {
  static JavaConfirmedTalk from(Proposal proposal) {
    return new JavaConfirmedTalk(
      proposal.id().get(),
      proposal.title(),
      proposal.format().name(),
      proposal.track().name(),
      proposal.speakers().stream().map(speaker -> speaker.email().get()).toList(),
      proposal.speakers().stream().anyMatch(SpeakerProfile::fromAbroad)
    );
  }
}
