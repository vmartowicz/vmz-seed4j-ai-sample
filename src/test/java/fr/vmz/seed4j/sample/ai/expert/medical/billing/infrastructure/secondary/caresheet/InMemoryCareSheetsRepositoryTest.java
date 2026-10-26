package fr.vmz.seed4j.sample.ai.expert.medical.billing.infrastructure.secondary.caresheet;

import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.expert.medical.billing.domain.ActToBill;
import fr.vmz.seed4j.sample.ai.expert.medical.billing.domain.CareSheet;
import fr.vmz.seed4j.sample.ai.expert.medical.billing.domain.CareSheetId;
import fr.vmz.seed4j.sample.ai.expert.medical.billing.domain.Insurance;
import fr.vmz.seed4j.sample.ai.expert.medical.billing.domain.Tariff;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.DoctorId;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.PatientId;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import org.junit.jupiter.api.Test;

@UnitTest
class InMemoryCareSheetsRepositoryTest {

  @Test
  void shouldGetSavedCareSheets() {
    var sheets = new InMemoryCareSheetsRepository();
    var patient = PatientId.newId();
    var sheet = CareSheet.issue(new ActToBill(UUID.randomUUID(), patient, DoctorId.newId(), LocalDate.of(2026, 6, 1)), new Insurance(patient, true, false, false), new Tariff(false, BigDecimal.ZERO));

    sheets.save(sheet);

    assertThat(sheets.get(sheet.id())).containsSame(sheet);
    assertThat(sheets.get(CareSheetId.newId())).isEmpty();
    assertThat(sheets.all()).containsExactly(sheet);
  }
}
