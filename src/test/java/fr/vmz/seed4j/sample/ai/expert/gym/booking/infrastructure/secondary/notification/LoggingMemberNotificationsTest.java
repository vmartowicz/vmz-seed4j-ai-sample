package fr.vmz.seed4j.sample.ai.expert.gym.booking.infrastructure.secondary.notification;

import ch.qos.logback.classic.Level;
import fr.vmz.seed4j.sample.ai.Logs;
import fr.vmz.seed4j.sample.ai.LogsSpy;
import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.shared.gym.domain.MemberId;
import org.junit.jupiter.api.Test;

@UnitTest
class LoggingMemberNotificationsTest {

  @Logs
  private LogsSpy logs;

  @Test
  void shouldLogNotification() {
    var member = MemberId.newId();

    new LoggingMemberNotifications().notify(member, "A spot opened up");

    logs.shouldHave(Level.INFO, "Notification to member " + member.get() + ": A spot opened up");
  }
}
