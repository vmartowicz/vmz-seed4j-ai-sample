package fr.vmz.seed4j.sample.ai.expert.medical.appointments.infrastructure.primary;

import static fr.vmz.seed4j.sample.ai.expert.medical.MedicalApi.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.vmz.seed4j.sample.ai.IntegrationTest;
import fr.vmz.seed4j.sample.ai.expert.medical.MedicalApi;
import fr.vmz.seed4j.sample.ai.expert.medical.MedicalClock;
import fr.vmz.seed4j.sample.ai.expert.medical.MedicalClockConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import java.util.UUID;

@IntegrationTest
@AutoConfigureMockMvc
@Import(MedicalClockConfiguration.class)
class AppointmentsResourceIT {

  @Autowired
  private MockMvc rest;

  @Autowired
  private MedicalClock clock;

  private MedicalApi api;

  @BeforeEach
  void loadApi() {
    api = new MedicalApi(rest);
    clock.at(NOW);
  }

  @Test
  void shouldRegisterPatientAndDoctor() throws Exception {
    var doctor = api.generalPractitioner();

    api
      .registerPatient("1990-05-04", "FEMALE", ",\"referringDoctorId\":\"" + doctor + "\",\"longTermIllness\":true")
      .andExpect(status().isCreated())
      .andExpect(jsonPath("$.lastName").value("Doe"))
      .andExpect(jsonPath("$.firstName").value("Jane"))
      .andExpect(jsonPath("$.sex").value("FEMALE"))
      .andExpect(jsonPath("$.socialSecurityNumber").value(SSN))
      .andExpect(jsonPath("$.referringDoctorId").value(doctor))
      .andExpect(jsonPath("$.longTermIllness").value(true))
      .andExpect(jsonPath("$.solidarityHealthCover").value(false));
  }

  @Test
  void shouldNotRegisterInvalidSocialSecurityNumber() throws Exception {
    api
      .postJson("/api/medical/patients", "{\"lastName\":\"Doe\",\"firstName\":\"Jane\",\"birthDate\":\"1990-05-04\",\"sex\":\"FEMALE\",\"socialSecurityNumber\":\"190057512345623\",\"phone\":\"06\"}")
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.title").value("Invalid value"));
  }

  @Test
  void shouldNotRegisterPatientWithoutName() throws Exception {
    api
      .postJson("/api/medical/patients", "{\"firstName\":\"Jane\",\"birthDate\":\"1990-05-04\",\"sex\":\"FEMALE\",\"socialSecurityNumber\":\"%s\",\"phone\":\"06\"}".formatted(SSN))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.title").value("Invalid value"));
  }

  @Test
  void shouldBookFreeSlotOnlineOnce() throws Exception {
    var doctor = api.generalPractitioner();
    var patient = api.patient();
    api.get("/api/medical/doctors/" + doctor + "/free-slots?date=2026-06-01").andExpect(jsonPath("$.length()").value(26));

    api
      .book(patient, doctor, MONDAY.atTime(9, 30), "ONLINE", null)
      .andExpect(status().isCreated())
      .andExpect(jsonPath("$.patientId").value(patient))
      .andExpect(jsonPath("$.doctorId").value(doctor))
      .andExpect(jsonPath("$.start").value(startsWith("2026-06-01T09:30")))
      .andExpect(jsonPath("$.end").value(startsWith("2026-06-01T09:45")))
      .andExpect(jsonPath("$.status").value("BOOKED"));

    api.book(api.patient(), doctor, MONDAY.atTime(9, 30), "PHONE", null).andExpect(status().isConflict());
    api.get("/api/medical/doctors/" + doctor + "/free-slots?date=2026-06-01").andExpect(jsonPath("$.length()").value(25));
  }

  @Test
  void shouldKeepEmergencySlotsForPhoneBookingsOfTheDay() throws Exception {
    var doctor = api.generalPractitioner();
    var patient = api.patient();

    api.book(patient, doctor, MONDAY.atTime(17, 30), "ONLINE", null).andExpect(status().isConflict()).andExpect(jsonPath("$.detail").value(containsString("emergenc")));
    api.book(patient, doctor, MONDAY.atTime(17, 30), "PHONE", null).andExpect(status().isCreated());
  }

  @Test
  void shouldCancelUntil24HoursBefore() throws Exception {
    var doctor = api.generalPractitioner();
    var tomorrow = read(api.book(api.patient(), doctor, MONDAY.plusDays(1).atTime(9, 0), "ONLINE", null), "$.id");
    var later = read(api.book(api.patient(), doctor, MONDAY.plusDays(1).atTime(8, 0).plusHours(1).plusMinutes(15), "ONLINE", null), "$.id");
    clock.at(MONDAY.atTime(9, 0));

    api.postJson("/api/medical/appointments/" + tomorrow + "/cancellation", "{}").andExpect(status().isOk()).andExpect(jsonPath("$.status").value("CANCELLED"));
    clock.at(MONDAY.atTime(9, 16));
    api.postJson("/api/medical/appointments/" + later + "/cancellation", "{}").andExpect(status().isConflict());
  }

  @Test
  void shouldBlockOnlineBookingAfter3NoShows() throws Exception {
    var doctor = api.generalPractitioner();
    var patient = api.patient();
    for (var time : new String[] { "09:00", "09:15", "09:30" }) {
      var appointment = read(api.book(patient, doctor, MONDAY.atTime(9, 0).withMinute(Integer.parseInt(time.substring(3))), "ONLINE", null), "$.id");
      clock.at(MONDAY.atTime(10, 0));
      api.postJson("/api/medical/appointments/" + appointment + "/no-show", "{}").andExpect(jsonPath("$.status").value("NO_SHOW"));
      clock.at(NOW);
    }

    api.book(patient, doctor, MONDAY.atTime(11, 0), "ONLINE", null).andExpect(status().isConflict()).andExpect(jsonPath("$.detail").value(containsString("phone")));
    var honored = read(api.book(patient, doctor, MONDAY.atTime(11, 0), "PHONE", null).andExpect(status().isCreated()), "$.id");
    api.postJson("/api/medical/appointments/" + honored + "/honored", "{}").andExpect(jsonPath("$.status").value("HONORED"));
  }

  @Test
  void shouldNeedCompanionForChildren() throws Exception {
    var doctor = api.generalPractitioner();
    var child = read(api.registerPatient("2016-01-01", "MALE", ""), "$.id");

    api.book(child, doctor, MONDAY.atTime(10, 0), "ONLINE", null).andExpect(status().isConflict());
    api.book(child, doctor, MONDAY.atTime(10, 0), "ONLINE", "Mum").andExpect(status().isCreated()).andExpect(jsonPath("$.companion").value("Mum"));
  }

  @Test
  void shouldKeepAppointmentsWhenReferringDoctorChanges() throws Exception {
    var doctor = api.generalPractitioner();
    var patient = api.patient();
    var appointment = read(api.book(patient, doctor, MONDAY.atTime(10, 0), "ONLINE", null), "$.id");
    var newDoctor = api.generalPractitioner();

    api.postJson("/api/medical/patients/" + patient + "/referring-doctor", "{\"doctorId\":\"" + newDoctor + "\"}").andExpect(jsonPath("$.referringDoctorId").value(newDoctor));

    api.get("/api/medical/appointments/" + appointment).andExpect(jsonPath("$.status").value("BOOKED")).andExpect(jsonPath("$.doctorId").value(doctor));
    api.get("/api/medical/patients/" + patient).andExpect(jsonPath("$.referringDoctorId").value(newDoctor));
  }

  @Test
  void shouldNotFindUnknownPatientDoctorOrAppointment() throws Exception {
    api.get("/api/medical/patients/" + UUID.randomUUID()).andExpect(status().isNotFound()).andExpect(jsonPath("$.title").value("Unknown element"));
    api.get("/api/medical/doctors/" + UUID.randomUUID() + "/free-slots?date=2026-06-01").andExpect(status().isNotFound());
    api.get("/api/medical/appointments/" + UUID.randomUUID()).andExpect(status().isNotFound());
  }
}
