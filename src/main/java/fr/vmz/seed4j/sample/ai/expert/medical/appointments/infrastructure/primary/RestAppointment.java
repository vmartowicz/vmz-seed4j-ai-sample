package fr.vmz.seed4j.sample.ai.expert.medical.appointments.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.medical.appointments.domain.Appointment;
import fr.vmz.seed4j.sample.ai.expert.medical.appointments.domain.AppointmentStatus;
import fr.vmz.seed4j.sample.ai.expert.medical.appointments.domain.Channel;
import java.time.LocalDateTime;
import java.util.UUID;
import org.jspecify.annotations.Nullable;

record RestAppointment(
  UUID id,
  UUID patientId,
  UUID doctorId,
  LocalDateTime start,
  LocalDateTime end,
  Channel channel,
  @Nullable String companion,
  AppointmentStatus status
) {
  static RestAppointment from(Appointment appointment) {
    return new RestAppointment(
      appointment.id().get(),
      appointment.patient().get(),
      appointment.doctor().get(),
      appointment.start(),
      appointment.end(),
      appointment.channel(),
      appointment.companion().orElse(null),
      appointment.status()
    );
  }
}
