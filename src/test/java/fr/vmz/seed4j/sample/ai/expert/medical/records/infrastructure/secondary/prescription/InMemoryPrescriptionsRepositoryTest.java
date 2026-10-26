package fr.vmz.seed4j.sample.ai.expert.medical.records.infrastructure.secondary.prescription;

import static fr.vmz.seed4j.sample.ai.expert.medical.records.domain.RecordsFixture.*;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.expert.medical.records.domain.MedicalRecord;
import fr.vmz.seed4j.sample.ai.expert.medical.records.domain.PrescriptionId;
import org.junit.jupiter.api.Test;

@UnitTest
class InMemoryPrescriptionsRepositoryTest {

  @Test
  void shouldGetSavedPrescription() {
    var prescriptions = new InMemoryPrescriptionsRepository();
    var consultation = openConsultation();
    var prescription = consultation.prescribe(items(medicine("Doliprane")), MedicalRecord.of(consultation.patient()), TODAY);

    prescriptions.save(prescription);

    assertThat(prescriptions.get(prescription.id())).containsSame(prescription);
    assertThat(prescriptions.get(PrescriptionId.newId())).isEmpty();
  }
}
