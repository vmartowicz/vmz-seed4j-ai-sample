package fr.vmz.seed4j.sample.ai.expert.medical.imaging.infrastructure.secondary.delivery;

import static fr.vmz.seed4j.sample.ai.expert.medical.imaging.domain.ImagingFixture.*;

import ch.qos.logback.classic.Level;
import fr.vmz.seed4j.sample.ai.Logs;
import fr.vmz.seed4j.sample.ai.LogsSpy;
import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.expert.medical.imaging.domain.ImagingExam;
import org.junit.jupiter.api.Test;

@UnitTest
class LoggingReportDeliveryTest {

  @Logs
  private LogsSpy logs;

  @Test
  void shouldLogDeliveryToPrescriberAndPatient() {
    var examination = performed(ImagingExam.XRAY);
    examination.report("No fracture", NOW);

    new LoggingReportDelivery().deliver(examination);

    logs.shouldHave(
      Level.INFO,
      "Report of XRAY " + examination.id().get() + " sent to doctor " + examination.prescriber().get() + " and patient " + examination.patient().get()
    );
  }
}
