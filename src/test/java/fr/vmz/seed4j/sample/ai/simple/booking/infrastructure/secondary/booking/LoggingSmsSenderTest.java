package fr.vmz.seed4j.sample.ai.simple.booking.infrastructure.secondary.booking;

import ch.qos.logback.classic.Level;
import fr.vmz.seed4j.sample.ai.Logs;
import fr.vmz.seed4j.sample.ai.LogsSpy;
import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.simple.booking.domain.booking.MobilePhone;
import fr.vmz.seed4j.sample.ai.simple.booking.domain.booking.Sms;
import org.junit.jupiter.api.Test;

@UnitTest
class LoggingSmsSenderTest {

  @Logs
  private LogsSpy logs;

  @Test
  void shouldLogSms() {
    new LoggingSmsSender().send(new Sms(new MobilePhone("0612345678"), "Rappel : votre rendez-vous Massage demain à 10:00"));

    logs.shouldHave(Level.INFO, "SMS to 0612345678: Rappel : votre rendez-vous Massage demain à 10:00");
  }
}
