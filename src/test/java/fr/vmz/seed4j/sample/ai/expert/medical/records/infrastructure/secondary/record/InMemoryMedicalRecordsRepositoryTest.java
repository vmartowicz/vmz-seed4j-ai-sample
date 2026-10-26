package fr.vmz.seed4j.sample.ai.expert.medical.records.infrastructure.secondary.record;

import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.expert.medical.records.domain.MedicalRecord;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.PatientId;
import org.junit.jupiter.api.Test;

@UnitTest
class InMemoryMedicalRecordsRepositoryTest {

  @Test
  void shouldGetSavedRecord() {
    var records = new InMemoryMedicalRecordsRepository();
    var record = MedicalRecord.of(PatientId.newId());

    records.save(record);

    assertThat(records.get(record.patient())).containsSame(record);
    assertThat(records.get(PatientId.newId())).isEmpty();
  }
}
