package fr.vmz.seed4j.sample.ai.expert.medical.appointments.infrastructure.secondary.appointment;

import static fr.vmz.seed4j.sample.ai.expert.medical.appointments.domain.AppointmentsFixture.*;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.expert.medical.appointments.domain.AppointmentId;
import fr.vmz.seed4j.sample.ai.expert.medical.appointments.domain.AppointmentToBook;
import fr.vmz.seed4j.sample.ai.expert.medical.appointments.domain.Channel;
import fr.vmz.seed4j.sample.ai.expert.medical.appointments.domain.Slot;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.DoctorId;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.PatientId;
import org.junit.jupiter.api.Test;

@UnitTest
class InMemoryAppointmentsRepositoryTest {

  @Test
  void shouldFindAppointmentsOfDoctorAndPatient() {
    var appointments = new InMemoryAppointmentsRepository();
    var patient = PatientId.newId();
    var doctor = DoctorId.newId();
    var appointment = new AppointmentToBook(patient, doctor, NOW, Channel.PHONE, null).book(new Slot(NOW, NOW.plusMinutes(15), false));
    appointments.save(appointment);
    appointments.save(new AppointmentToBook(PatientId.newId(), DoctorId.newId(), NOW, Channel.PHONE, null).book(new Slot(NOW, NOW.plusMinutes(15), false)));

    assertThat(appointments.get(appointment.id())).containsSame(appointment);
    assertThat(appointments.get(AppointmentId.newId())).isEmpty();
    assertThat(appointments.ofDoctor(doctor)).containsExactly(appointment);
    assertThat(appointments.ofPatient(patient)).containsExactly(appointment);
  }
}
