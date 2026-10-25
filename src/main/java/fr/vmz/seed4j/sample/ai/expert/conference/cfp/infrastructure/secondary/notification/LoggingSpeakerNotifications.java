package fr.vmz.seed4j.sample.ai.expert.conference.cfp.infrastructure.secondary.notification;

import fr.vmz.seed4j.sample.ai.expert.conference.cfp.domain.SpeakerNotifications;
import fr.vmz.seed4j.sample.ai.shared.conference.domain.Email;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
class LoggingSpeakerNotifications implements SpeakerNotifications {

  private static final Logger log = LoggerFactory.getLogger(LoggingSpeakerNotifications.class);

  @Override
  public void notify(Email speaker, String message) {
    log.info("Notification to speaker {}: {}", speaker.get(), message);
  }
}
