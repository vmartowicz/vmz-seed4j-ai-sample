package fr.vmz.seed4j.sample.ai.expert.conference.cfp.domain;

import fr.vmz.seed4j.sample.ai.shared.conference.domain.Email;

public interface SpeakerTickets {
  void issueFor(SpeakerProfile speaker);

  void revokeFor(Email speaker);
}
