package fr.vmz.seed4j.sample.ai.expert.medical.laboratory.infrastructure.secondary.alert;

import ch.qos.logback.classic.Level;
import fr.vmz.seed4j.sample.ai.Logs;
import fr.vmz.seed4j.sample.ai.LogsSpy;
import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.expert.medical.laboratory.domain.CriticalAlert;
import fr.vmz.seed4j.sample.ai.expert.medical.laboratory.domain.LabAnalysis;
import fr.vmz.seed4j.sample.ai.expert.medical.laboratory.domain.SampleId;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.DoctorId;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.PatientId;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

@UnitTest
class LoggingCriticalAlertsTest {

  @Logs
  private LogsSpy logs;

  @Test
  void shouldLogCriticalAlert() {
    var alert = new CriticalAlert(SampleId.newId(), PatientId.newId(), LabAnalysis.POTASSIUM, new BigDecimal("6.5"), DoctorId.newId(), LocalDateTime.of(2026, 6, 6, 21, 0));

    new LoggingCriticalAlerts().send(alert);

    logs.shouldHave(
      Level.WARN,
      "Critical POTASSIUM result 6.5 for patient " + alert.patient().get() + " (sample " + alert.sample().get() + "): doctor " + alert.recipient().get() + " alerted"
    );
  }
}
