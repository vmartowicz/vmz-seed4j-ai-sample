package fr.vmz.seed4j.sample.ai.expert.medical.appointments.infrastructure.secondary.doctor;

import static fr.vmz.seed4j.sample.ai.expert.medical.appointments.domain.AppointmentsFixture.*;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.DoctorId;
import org.junit.jupiter.api.Test;

@UnitTest
class InMemoryDoctorsRepositoryTest {

  @Test
  void shouldGetSavedDoctors() {
    var doctors = new InMemoryDoctorsRepository();
    var doctor = generalPractitioner();

    doctors.save(doctor);

    assertThat(doctors.get(doctor.id())).containsSame(doctor);
    assertThat(doctors.get(DoctorId.newId())).isEmpty();
    assertThat(doctors.all()).containsExactly(doctor);
  }
}
