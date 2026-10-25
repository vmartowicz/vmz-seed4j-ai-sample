package fr.vmz.seed4j.sample.ai.expert.conference.cfp.domain;

import fr.vmz.seed4j.sample.ai.shared.conference.domain.Email;

public interface SpeakerNotifications {
  void notify(Email speaker, String message);
}
