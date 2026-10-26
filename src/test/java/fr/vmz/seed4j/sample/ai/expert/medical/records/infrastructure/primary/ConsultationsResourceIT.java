package fr.vmz.seed4j.sample.ai.expert.medical.records.infrastructure.primary;

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
class ConsultationsResourceIT {

  @Autowired
  private MockMvc rest;

  @Autowired
  private MedicalClock clock;

  private MedicalApi api;

  @BeforeEach
  void loadApi() {
    api = new MedicalApi(rest);
    clock.at(NOW.plusHours(2));
  }

  @Test
  void shouldKeepMedicalRecord() throws Exception {
    var patient = api.patient();

    api.postJson("/api/medical/records/" + patient + "/allergies", "{\"text\":\"Penicillin\"}").andExpect(status().isOk());
    api.postJson("/api/medical/records/" + patient + "/history", "{\"text\":\"Asthma\"}").andExpect(status().isOk());
    api.postJson("/api/medical/records/" + patient + "/treatments", "{\"text\":\"Ventoline\"}").andExpect(status().isOk());
    api.postJson("/api/medical/records/" + patient + "/pacemaker", "{}").andExpect(status().isOk());

    api
      .get("/api/medical/records/" + patient)
      .andExpect(jsonPath("$.patientId").value(patient))
      .andExpect(jsonPath("$.allergies").value(contains("penicillin")))
      .andExpect(jsonPath("$.history").value(contains("Asthma")))
      .andExpect(jsonPath("$.treatments").value(contains("Ventoline")))
      .andExpect(jsonPath("$.pacemaker").value(true));
  }

  @Test
  void shouldConsultPrescribeCloseAndBill() throws Exception {
    var doctor = api.generalPractitioner();
    var patient = api.patient(",\"referringDoctorId\":\"%s\"".formatted(api.generalPractitioner()));
    api.postJson("/api/medical/records/" + patient + "/allergies", "{\"text\":\"Penicillin\"}");
    var consultation = api.consultation(patient, doctor);

    api
      .postJson("/api/medical/consultations/" + consultation + "/notes", "{\"examination\":\"Red throat\",\"diagnosis\":\"Pharyngitis\"}")
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.diagnosis").value("Pharyngitis"));
    api.prescribe(consultation, "MEDICINE", "penicillin").andExpect(status().isConflict()).andExpect(jsonPath("$.detail").value(containsString("allergic")));
    api
      .prescribe(consultation, "MEDICINE", "Doliprane")
      .andExpect(status().isCreated())
      .andExpect(jsonPath("$.items[0].kind").value("MEDICINE"))
      .andExpect(jsonPath("$.items[0].code").value("Doliprane"))
      .andExpect(jsonPath("$.items[0].validUntil").value("2026-09-01"));

    api.close(consultation).andExpect(status().isOk()).andExpect(jsonPath("$.status").value("CLOSED"));

    api.postJson("/api/medical/consultations/" + consultation + "/notes", "{\"examination\":\"x\",\"diagnosis\":\"y\"}").andExpect(status().isConflict());
    api
      .postJson("/api/medical/consultations/" + consultation + "/addenda", "{\"doctorId\":\"" + doctor + "\",\"text\":\"Fever gone\"}")
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.addenda[0].text").value("Fever gone"));
    api
      .get("/api/medical/care-sheets?patient=" + patient)
      .andExpect(jsonPath("$[0].consultationId").value(consultation))
      .andExpect(jsonPath("$[0].baseFee").value(30))
      .andExpect(jsonPath("$[0].reimbursement").value(21))
      .andExpect(jsonPath("$[0].status").value("PENDING"));
    api.get("/api/medical/consultations/" + consultation).andExpect(jsonPath("$.prescriptionId").isNotEmpty());
  }

  @Test
  void shouldNotAddAddendumToOpenConsultation() throws Exception {
    var doctor = api.generalPractitioner();
    var consultation = api.consultation(api.patient(), doctor);

    api.postJson("/api/medical/consultations/" + consultation + "/addenda", "{\"doctorId\":\"" + doctor + "\",\"text\":\"Early\"}").andExpect(status().isConflict());
  }

  @Test
  void shouldNotConsultUnknownPatient() throws Exception {
    api
      .postJson("/api/medical/consultations", "{\"patientId\":\"%s\",\"doctorId\":\"%s\",\"reason\":\"Check-up\"}".formatted(UUID.randomUUID(), api.generalPractitioner()))
      .andExpect(status().isNotFound())
      .andExpect(jsonPath("$.title").value("Unknown element"));
  }

  @Test
  void shouldNotConsultWithoutReason() throws Exception {
    api
      .postJson("/api/medical/consultations", "{\"patientId\":\"%s\",\"doctorId\":\"%s\"}".formatted(api.patient(), api.generalPractitioner()))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.title").value("Invalid value"));
  }
}
