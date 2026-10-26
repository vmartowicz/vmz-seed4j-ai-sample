package fr.vmz.seed4j.sample.ai.expert.medical.appointments.domain;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.DoctorId;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.PatientId;
import java.time.LocalDateTime;
import org.jspecify.annotations.Nullable;

public record AppointmentToBook(PatientId patient, DoctorId doctor, LocalDateTime start, Channel channel, @Nullable String companion) {
  public AppointmentToBook {
    Assert.notNull("patient", patient);
    Assert.notNull("doctor", doctor);
    Assert.notNull("start", start);
    Assert.notNull("channel", channel);

    companion = companion == null || companion.isBlank() ? null : companion.trim();
  }

  public Appointment book(Slot slot) {
    return new Appointment(this, slot);
  }
}
