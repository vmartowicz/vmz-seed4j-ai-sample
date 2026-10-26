package fr.vmz.seed4j.sample.ai.expert.medical.appointments.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.medical.appointments.application.AppointmentsApplicationService;
import fr.vmz.seed4j.sample.ai.expert.medical.appointments.domain.AppointmentId;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.DoctorId;
import fr.vmz.seed4j.sample.ai.shared.medical.domain.PatientId;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/medical")
class AppointmentsResource {

  private final AppointmentsApplicationService appointments;

  AppointmentsResource(AppointmentsApplicationService appointments) {
    this.appointments = appointments;
  }

  @PostMapping("/doctors")
  @ResponseStatus(HttpStatus.CREATED)
  RestDoctor registerDoctor(@RequestBody RestDoctorToRegister doctor) {
    return RestDoctor.from(appointments.registerDoctor(doctor.toDomain()));
  }

  @GetMapping("/doctors/{doctorId}/free-slots")
  List<RestSlot> freeSlots(@PathVariable UUID doctorId, @RequestParam LocalDate date) {
    return appointments.freeSlots(new DoctorId(doctorId), date).stream().map(RestSlot::from).toList();
  }

  @PostMapping("/patients")
  @ResponseStatus(HttpStatus.CREATED)
  RestPatient registerPatient(@RequestBody RestPatientToRegister patient) {
    return RestPatient.from(appointments.registerPatient(patient.toDomain()));
  }

  @GetMapping("/patients/{patientId}")
  RestPatient patient(@PathVariable UUID patientId) {
    return RestPatient.from(appointments.patient(new PatientId(patientId)));
  }

  @PostMapping("/patients/{patientId}/referring-doctor")
  RestPatient changeReferringDoctor(@PathVariable UUID patientId, @RequestBody RestReference reference) {
    return RestPatient.from(appointments.changeReferringDoctor(new PatientId(patientId), new DoctorId(reference.doctorId())));
  }

  @PostMapping("/patients/{patientId}/insured")
  RestPatient attach(@PathVariable UUID patientId, @RequestBody RestReference reference) {
    return RestPatient.from(appointments.attach(new PatientId(patientId), new PatientId(reference.insuredId())));
  }

  @PostMapping("/appointments")
  @ResponseStatus(HttpStatus.CREATED)
  RestAppointment book(@RequestBody RestAppointmentToBook booking) {
    return RestAppointment.from(appointments.book(booking.toDomain()));
  }

  @GetMapping("/appointments/{appointmentId}")
  RestAppointment appointment(@PathVariable UUID appointmentId) {
    return RestAppointment.from(appointments.appointment(new AppointmentId(appointmentId)));
  }

  @PostMapping("/appointments/{appointmentId}/cancellation")
  RestAppointment cancel(@PathVariable UUID appointmentId) {
    return RestAppointment.from(appointments.cancel(new AppointmentId(appointmentId)));
  }

  @PostMapping("/appointments/{appointmentId}/honored")
  RestAppointment honor(@PathVariable UUID appointmentId) {
    return RestAppointment.from(appointments.honor(new AppointmentId(appointmentId)));
  }

  @PostMapping("/appointments/{appointmentId}/no-show")
  RestAppointment markNoShow(@PathVariable UUID appointmentId) {
    return RestAppointment.from(appointments.markNoShow(new AppointmentId(appointmentId)));
  }
}
