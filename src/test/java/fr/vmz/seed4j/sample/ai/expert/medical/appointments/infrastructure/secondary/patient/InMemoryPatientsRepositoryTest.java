package fr.vmz.seed4j.sample.ai.expert.medical.appointments.infrastructure.secondary.patient;

import static fr.vmz.seed4j.sample.ai.expert.medical.appointments.domain.AppointmentsFixture.*;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.PatientId;
import org.junit.jupiter.api.Test;

@UnitTest
class InMemoryPatientsRepositoryTest {

  @Test
  void shouldGetSavedPatient() {
    var patients = new InMemoryPatientsRepository();
    var patient = patient();

    patients.save(patient);

    assertThat(patients.get(patient.id())).containsSame(patient);
    assertThat(patients.get(PatientId.newId())).isEmpty();
  }
}
