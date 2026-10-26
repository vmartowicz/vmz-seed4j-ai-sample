package fr.vmz.seed4j.sample.ai.expert.medical.appointments.application;

import fr.vmz.seed4j.sample.ai.expert.medical.appointments.domain.Appointment;
import fr.vmz.seed4j.sample.ai.expert.medical.appointments.domain.AppointmentDesk;
import fr.vmz.seed4j.sample.ai.expert.medical.appointments.domain.AppointmentId;
import fr.vmz.seed4j.sample.ai.expert.medical.appointments.domain.AppointmentToBook;
import fr.vmz.seed4j.sample.ai.expert.medical.appointments.domain.AppointmentsRepository;
import fr.vmz.seed4j.sample.ai.expert.medical.appointments.domain.Doctor;
import fr.vmz.seed4j.sample.ai.expert.medical.appointments.domain.DoctorToRegister;
import fr.vmz.seed4j.sample.ai.expert.medical.appointments.domain.DoctorsRepository;
import fr.vmz.seed4j.sample.ai.expert.medical.appointments.domain.Patient;
import fr.vmz.seed4j.sample.ai.expert.medical.appointments.domain.PatientToRegister;
import fr.vmz.seed4j.sample.ai.expert.medical.appointments.domain.PatientsRepository;
import fr.vmz.seed4j.sample.ai.expert.medical.appointments.domain.Slot;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.DoctorId;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.PatientId;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class AppointmentsApplicationService {

  private final PatientsRepository patients;
  private final DoctorsRepository doctors;
  private final AppointmentDesk desk;
  private final Clock clock;

  public AppointmentsApplicationService(PatientsRepository patients, DoctorsRepository doctors, AppointmentsRepository appointments, Clock clock) {
    this.patients = patients;
    this.doctors = doctors;
    desk = new AppointmentDesk(patients, doctors, appointments);
    this.clock = clock;
  }

  public Doctor registerDoctor(DoctorToRegister doctor) {
    return desk.registerDoctor(doctor);
  }

  public Patient registerPatient(PatientToRegister patient) {
    return desk.registerPatient(patient);
  }

  public Patient patient(PatientId patient) {
    return desk.patient(patient);
  }

  public Optional<Patient> findPatient(PatientId patient) {
    return patients.get(patient);
  }

  public Optional<Doctor> findDoctor(DoctorId doctor) {
    return doctors.get(doctor);
  }

  public Optional<DoctorId> onCallReplacing(DoctorId doctor) {
    return doctors.get(doctor).flatMap(found -> desk.onCallDoctorOf(found.centre())).map(Doctor::id);
  }

  public Patient changeReferringDoctor(PatientId patient, DoctorId doctor) {
    return desk.changeReferringDoctor(patient, doctor);
  }

  public Patient attach(PatientId patient, PatientId insured) {
    return desk.attach(patient, insured);
  }

  public List<Slot> freeSlots(DoctorId doctor, LocalDate date) {
    return desk.freeSlots(doctor, date, now());
  }

  public Appointment book(AppointmentToBook booking) {
    return desk.book(booking, now());
  }

  public Appointment appointment(AppointmentId appointment) {
    return desk.appointment(appointment);
  }

  public Appointment cancel(AppointmentId appointment) {
    return desk.cancel(appointment, now());
  }

  public Appointment honor(AppointmentId appointment) {
    return desk.honor(appointment);
  }

  public Appointment markNoShow(AppointmentId appointment) {
    return desk.markNoShow(appointment, now());
  }

  private LocalDateTime now() {
    return LocalDateTime.now(clock);
  }
}
