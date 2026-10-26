package fr.vmz.seed4j.sample.ai.expert.medical.billing.infrastructure.secondary.insurer;

import ch.qos.logback.classic.Level;
import fr.vmz.seed4j.sample.ai.Logs;
import fr.vmz.seed4j.sample.ai.LogsSpy;
import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.expert.medical.billing.domain.ActToBill;
import fr.vmz.seed4j.sample.ai.expert.medical.billing.domain.CareSheet;
import fr.vmz.seed4j.sample.ai.expert.medical.billing.domain.Insurance;
import fr.vmz.seed4j.sample.ai.expert.medical.billing.domain.Tariff;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.DoctorId;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.PatientId;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import org.junit.jupiter.api.Test;

@UnitTest
class LoggingHealthInsurerTest {

  @Logs
  private LogsSpy logs;

  @Test
  void shouldLogSentCareSheet() {
    var patient = PatientId.newId();
    var sheet = CareSheet.issue(new ActToBill(UUID.randomUUID(), patient, DoctorId.newId(), LocalDate.of(2026, 6, 1)), new Insurance(patient, true, false, false), new Tariff(false, BigDecimal.ZERO));

    new LoggingHealthInsurer().send(sheet);

    logs.shouldHave(Level.INFO, "Care sheet " + sheet.id().get() + " sent for insured " + patient.get() + ": 21.00 reimbursed");
  }
}
