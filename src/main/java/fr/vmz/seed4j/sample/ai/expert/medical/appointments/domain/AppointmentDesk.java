package fr.vmz.seed4j.sample.ai.expert.medical.appointments.domain;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.DoctorId;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.PatientId;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class AppointmentDesk {

  private static final int MAX_NO_SHOWS = 3;
  private static final int NO_SHOW_MONTHS = 12;

  private final PatientsRepository patients;
  private final DoctorsRepository doctors;
  private final AppointmentsRepository appointments;

  public AppointmentDesk(PatientsRepository patients, DoctorsRepository doctors, AppointmentsRepository appointments) {
    this.patients = patients;
    this.doctors = doctors;
    this.appointments = appointments;
  }

  public Doctor registerDoctor(DoctorToRegister toRegister) {
    var doctor = toRegister.register();

    doctors.save(doctor);

    return doctor;
  }

  public Patient registerPatient(PatientToRegister toRegister) {
    var patient = toRegister.register();

    patients.save(patient);

    return patient;
  }

  public Appointment book(AppointmentToBook booking, LocalDateTime now) {
    Assert.notNull("booking", booking);

    var patient = patients.patientOf(booking.patient());
    var doctor = doctors.doctorOf(booking.doctor());
    var slot = Agenda.slotAt(doctor, booking.start())
      .filter(found -> found.start().isAfter(now))
      .orElseThrow(() -> new NotASlotException(booking.start()));

    if (slot.emergency() && (booking.channel() != Channel.PHONE || !slot.start().toLocalDate().equals(now.toLocalDate()))) {
      throw new EmergencySlotException(slot.start());
    }

    if (appointments.ofDoctor(doctor.id()).stream().anyMatch(other -> other.isBooked() && other.start().equals(slot.start()))) {
      throw new SlotTakenException(slot.start());
    }

    if (booking.channel() == Channel.ONLINE && noShowsInLastYear(patient.id(), now) >= MAX_NO_SHOWS) {
      throw new OnlineBookingBlockedException(patient.id());
    }

    if (patient.isUnder16On(slot.start().toLocalDate()) && booking.companion() == null) {
      throw new CompanionRequiredException(patient.id());
    }

    var appointment = booking.book(slot);
    appointments.save(appointment);

    return appointment;
  }

  public Appointment cancel(AppointmentId id, LocalDateTime now) {
    var appointment = appointments.appointmentOf(id);

    appointment.cancel(now);
    appointments.save(appointment);

    return appointment;
  }

  public Appointment honor(AppointmentId id) {
    var appointment = appointments.appointmentOf(id);

    appointment.honor();
    appointments.save(appointment);

    return appointment;
  }

  public Appointment markNoShow(AppointmentId id, LocalDateTime now) {
    var appointment = appointments.appointmentOf(id);

    appointment.markNoShow(now);
    appointments.save(appointment);

    return appointment;
  }

  public List<Slot> freeSlots(DoctorId doctorId, LocalDate date, LocalDateTime now) {
    var doctor = doctors.doctorOf(doctorId);
    var taken = appointments.ofDoctor(doctor.id()).stream().filter(Appointment::isBooked).map(Appointment::start).toList();

    return Agenda.slotsOf(doctor, date)
      .stream()
      .filter(slot -> !slot.emergency() && slot.start().isAfter(now) && !taken.contains(slot.start()))
      .toList();
  }

  public Patient changeReferringDoctor(PatientId patientId, DoctorId doctor) {
    var patient = patients.patientOf(patientId);

    patient.changeReferringDoctor(doctors.doctorOf(doctor).id());
    patients.save(patient);

    return patient;
  }

  public Patient attach(PatientId patientId, PatientId insured) {
    var patient = patients.patientOf(patientId);

    patient.attachTo(patients.patientOf(insured).id());
    patients.save(patient);

    return patient;
  }

  public Optional<Doctor> onCallDoctorOf(Centre centre) {
    return doctors
      .all()
      .stream()
      .filter(doctor -> doctor.onCall() && doctor.centre() == centre)
      .findFirst();
  }

  public Patient patient(PatientId patient) {
    return patients.patientOf(patient);
  }

  public Doctor doctor(DoctorId doctor) {
    return doctors.doctorOf(doctor);
  }

  public Appointment appointment(AppointmentId appointment) {
    return appointments.appointmentOf(appointment);
  }

  private long noShowsInLastYear(PatientId patient, LocalDateTime now) {
    var since = now.minusMonths(NO_SHOW_MONTHS);

    return appointments
      .ofPatient(patient)
      .stream()
      .filter(appointment -> appointment.status() == AppointmentStatus.NO_SHOW && appointment.start().isAfter(since))
      .count();
  }
}
