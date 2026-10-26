package fr.vmz.seed4j.sample.ai.expert.medical.appointments.domain;

import fr.vmz.seed4j.sample.ai.shared.medical.domain.DoctorId;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.PatientId;
import java.time.LocalDateTime;
import java.util.Optional;
import org.jspecify.annotations.Nullable;

public final class Appointment {

  private static final int CANCELLATION_HOURS = 24;

  private final AppointmentId id;
  private final PatientId patient;
  private final DoctorId doctor;
  private final LocalDateTime start;
  private final LocalDateTime end;
  private final Channel channel;
  private final @Nullable String companion;
  private AppointmentStatus status;

  Appointment(AppointmentToBook booking, Slot slot) {
    id = AppointmentId.newId();
    patient = booking.patient();
    doctor = booking.doctor();
    start = slot.start();
    end = slot.end();
    channel = booking.channel();
    companion = booking.companion();
    status = AppointmentStatus.BOOKED;
  }

  public void cancel(LocalDateTime now) {
    assertBooked("cancel");
    if (now.isAfter(start.minusHours(CANCELLATION_HOURS))) {
      throw new CancellationTooLateException(id);
    }

    status = AppointmentStatus.CANCELLED;
  }

  public void honor() {
    assertBooked("honor");

    status = AppointmentStatus.HONORED;
  }

  public void markNoShow(LocalDateTime now) {
    assertBooked("mark as missed");
    if (now.isBefore(start)) {
      throw new InvalidAppointmentTransitionException(id, status, "mark as missed before its start");
    }

    status = AppointmentStatus.NO_SHOW;
  }

  public boolean isBooked() {
    return status == AppointmentStatus.BOOKED;
  }

  private void assertBooked(String action) {
    if (!isBooked()) {
      throw new InvalidAppointmentTransitionException(id, status, action);
    }
  }

  public AppointmentId id() {
    return id;
  }

  public PatientId patient() {
    return patient;
  }

  public DoctorId doctor() {
    return doctor;
  }

  public LocalDateTime start() {
    return start;
  }

  public LocalDateTime end() {
    return end;
  }

  public Channel channel() {
    return channel;
  }

  public Optional<String> companion() {
    return Optional.ofNullable(companion);
  }

  public AppointmentStatus status() {
    return status;
  }
}
