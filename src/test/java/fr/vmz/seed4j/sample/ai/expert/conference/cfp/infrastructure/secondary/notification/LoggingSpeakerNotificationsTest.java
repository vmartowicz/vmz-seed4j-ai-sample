package fr.vmz.seed4j.sample.ai.expert.conference.cfp.infrastructure.secondary.notification;

import ch.qos.logback.classic.Level;
import fr.vmz.seed4j.sample.ai.Logs;
import fr.vmz.seed4j.sample.ai.LogsSpy;
import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.shared.conference.domain.Email;
import org.junit.jupiter.api.Test;

@UnitTest
class LoggingSpeakerNotificationsTest {

  @Logs
  private LogsSpy logs;

  @Test
  void shouldLogNotification() {
    new LoggingSpeakerNotifications().notify(new Email("jane@acme.com"), "Your proposal is accepted");

    logs.shouldHave(Level.INFO, "Notification to speaker jane@acme.com: Your proposal is accepted");
  }
}
