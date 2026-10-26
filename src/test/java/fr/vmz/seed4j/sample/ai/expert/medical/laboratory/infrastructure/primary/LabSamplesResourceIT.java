package fr.vmz.seed4j.sample.ai.expert.medical.laboratory.infrastructure.primary;

import static fr.vmz.seed4j.sample.ai.expert.medical.MedicalApi.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.vmz.seed4j.sample.ai.IntegrationTest;
import fr.vmz.seed4j.sample.ai.expert.medical.MedicalApi;
import fr.vmz.seed4j.sample.ai.expert.medical.MedicalClock;
import fr.vmz.seed4j.sample.ai.expert.medical.MedicalClockConfiguration;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

@IntegrationTest
@AutoConfigureMockMvc
@Import(MedicalClockConfiguration.class)
class LabSamplesResourceIT {

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
  void shouldTakeAnalyseAndValidateSample() throws Exception {
    var patient = api.patient();
    var prescription = api.prescription(patient, api.generalPractitioner(), "LAB_TEST", "POTASSIUM");

    var sample = read(
      api
        .sample(patient, prescription, "POTASSIUM", false)
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.test").value("POTASSIUM"))
        .andExpect(jsonPath("$.status").value("TAKEN")),
      "$.id"
    );
    api
      .postJson("/api/medical/laboratory/samples/" + sample + "/analysis", "{\"value\":4.2}")
      .andExpect(jsonPath("$.status").value("ANALYSED"))
      .andExpect(jsonPath("$.value").value(4.2));
    api
      .postJson("/api/medical/laboratory/samples/" + sample + "/validation", "{\"biologist\":\"Dr Bio\"}")
      .andExpect(jsonPath("$.status").value("VALIDATED"))
      .andExpect(jsonPath("$.validatedBy").value("Dr Bio"))
      .andExpect(jsonPath("$.abnormal").value(false))
      .andExpect(jsonPath("$.alertedDoctorId").doesNotExist());

    api.get("/api/medical/laboratory/patients/" + patient + "/results").andExpect(jsonPath("$[0].id").value(sample));
  }

  @Test
  void shouldRefuseFastingTestWhenPatientIsNotFasting() throws Exception {
    var patient = api.patient();
    var prescription = api.prescription(patient, api.generalPractitioner(), "LAB_TEST", "GLUCOSE");

    api.sample(patient, prescription, "GLUCOSE", false).andExpect(status().isConflict()).andExpect(jsonPath("$.detail").value(containsString("fasting")));
    api.sample(patient, prescription, "POTASSIUM", false).andExpect(status().isConflict());
  }

  @Test
  void shouldAskForNewSampleAfter4Hours() throws Exception {
    var patient = api.patient();
    var prescription = api.prescription(patient, api.generalPractitioner(), "LAB_TEST", "CREATININE");
    var sample = read(api.sample(patient, prescription, "CREATININE", false), "$.id");
    clock.at(NOW.plusHours(6).plusMinutes(1));

    api.postJson("/api/medical/laboratory/samples/" + sample + "/analysis", "{\"value\":9}").andExpect(status().isConflict());
  }

  @Test
  void shouldAlertOnCallDoctorOfCriticalResultOnSaturdayEvening() throws Exception {
    clock.at(LocalDateTime.of(2026, 6, 6, 20, 30));
    var prescriber = api.generalPractitioner();
    api.doctor("GENERAL_PRACTITIONER", "LYON_7", "0", true);
    var patient = api.patient();
    var prescription = api.prescription(patient, prescriber, "LAB_TEST", "POTASSIUM");
    var sample = read(api.sample(patient, prescription, "POTASSIUM", false), "$.id");
    api.postJson("/api/medical/laboratory/samples/" + sample + "/analysis", "{\"value\":6.8}");

    api
      .postJson("/api/medical/laboratory/samples/" + sample + "/validation", "{\"biologist\":\"Dr Bio\"}")
      .andExpect(jsonPath("$.critical").value(true))
      .andExpect(jsonPath("$.alertedDoctorId").value(not(prescriber)));
  }

  @Test
  void shouldShowAbnormalResultToPatientOnlyOnceAnnounced() throws Exception {
    var patient = api.patient();
    var sample = api.validatedSample(patient, "CREATININE", "20");

    api.get("/api/medical/laboratory/patients/" + patient + "/results").andExpect(jsonPath("$.length()").value(0));
    api
      .postJson("/api/medical/laboratory/samples/" + sample + "/announcement", "{\"doctorId\":\"" + api.generalPractitioner() + "\"}")
      .andExpect(jsonPath("$.announcedBy").isNotEmpty());
    api.get("/api/medical/laboratory/patients/" + patient + "/results").andExpect(jsonPath("$[0].abnormal").value(true));
    api.get("/api/medical/laboratory/samples/" + sample).andExpect(jsonPath("$.prescriptionId").isNotEmpty());
  }

  @Test
  void shouldNotFindUnknownPrescriptionOrSample() throws Exception {
    api
      .sample(api.patient(), UUID.randomUUID().toString(), "POTASSIUM", false)
      .andExpect(status().isNotFound())
      .andExpect(jsonPath("$.title").value("Unknown element"));
    api.get("/api/medical/laboratory/samples/" + UUID.randomUUID()).andExpect(status().isNotFound());
  }

  @Test
  void shouldNotTakeSampleWithoutTest() throws Exception {
    api
      .postJson("/api/medical/laboratory/samples", "{\"patientId\":\"%s\",\"prescriptionId\":\"%s\"}".formatted(api.patient(), UUID.randomUUID()))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.title").value("Invalid value"));
  }
}
