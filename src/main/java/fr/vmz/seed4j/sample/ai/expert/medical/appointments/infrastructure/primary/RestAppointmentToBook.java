package fr.vmz.seed4j.sample.ai.expert.medical.appointments.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.medical.appointments.domain.AppointmentToBook;
import fr.vmz.seed4j.sample.ai.expert.medical.appointments.domain.Channel;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.DoctorId;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.PatientId;
import java.time.LocalDateTime;
import java.util.UUID;
import org.jspecify.annotations.Nullable;

record RestAppointmentToBook(@Nullable UUID patientId, @Nullable UUID doctorId, @Nullable LocalDateTime start, @Nullable Channel channel, @Nullable String companion) {
  AppointmentToBook toDomain() {
    return new AppointmentToBook(new PatientId(patientId), new DoctorId(doctorId), start, channel, companion);
  }
}
