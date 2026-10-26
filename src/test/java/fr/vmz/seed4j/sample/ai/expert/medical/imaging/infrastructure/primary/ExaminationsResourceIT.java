package fr.vmz.seed4j.sample.ai.expert.medical.imaging.infrastructure.primary;

import static fr.vmz.seed4j.sample.ai.expert.medical.MedicalApi.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.vmz.seed4j.sample.ai.IntegrationTest;
import fr.vmz.seed4j.sample.ai.expert.medical.MedicalApi;
import fr.vmz.seed4j.sample.ai.expert.medical.MedicalClock;
import fr.vmz.seed4j.sample.ai.expert.medical.MedicalClockConfiguration;
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
class ExaminationsResourceIT {

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
  void shouldPerformAndReportExamination() throws Exception {
    var patient = read(api.registerPatient("1980-01-01", "MALE", ""), "$.id");
    var prescription = api.prescription(patient, api.generalPractitioner(), "IMAGING", "XRAY");

    var examination = read(
      api
        .examination(patient, prescription, "XRAY", false)
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.status").value("PERFORMED"))
        .andExpect(jsonPath("$.imagesAvailable").value(true))
        .andExpect(jsonPath("$.report").doesNotExist()),
      "$.id"
    );

    api
      .postJson("/api/medical/imaging/examinations/" + examination + "/report", "{\"text\":\"No fracture\"}")
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.status").value("REPORTED"))
      .andExpect(jsonPath("$.report").value("No fracture"));
    api.get("/api/medical/imaging/examinations/" + examination).andExpect(jsonPath("$.reportedAt").isNotEmpty());
  }

  @Test
  void shouldNeedNegativePregnancyTestForWomen() throws Exception {
    var patient = api.patient();
    var prescription = api.prescription(patient, api.generalPractitioner(), "IMAGING", "CT");

    api.examination(patient, prescription, "CT", false).andExpect(status().isConflict()).andExpect(jsonPath("$.detail").value(containsString("pregnancy")));
    api.examination(patient, prescription, "CT", true).andExpect(status().isCreated());
  }

  @Test
  void shouldNeedRecentCreatinineForContrastScan() throws Exception {
    var patient = read(api.registerPatient("1980-01-01", "MALE", ""), "$.id");
    var prescription = api.prescription(patient, api.generalPractitioner(), "IMAGING", "CT_CONTRAST");

    api.examination(patient, prescription, "CT_CONTRAST", false).andExpect(status().isConflict()).andExpect(jsonPath("$.detail").value(containsString("creatinine")));
    api.validatedSample(patient, "CREATININE", "9");
    api.examination(patient, prescription, "CT_CONTRAST", false).andExpect(status().isCreated());
  }

  @Test
  void shouldRefuseMriToPacemakerWearers() throws Exception {
    var patient = read(api.registerPatient("1980-01-01", "MALE", ""), "$.id");
    var prescription = api.prescription(patient, api.generalPractitioner(), "IMAGING", "MRI");
    api.postJson("/api/medical/records/" + patient + "/pacemaker", "{}");

    api.examination(patient, prescription, "MRI", false).andExpect(status().isConflict()).andExpect(jsonPath("$.detail").value(containsString("pacemaker")));
  }

  @Test
  void shouldListOverdueExaminations() throws Exception {
    var patient = read(api.registerPatient("1980-01-01", "MALE", ""), "$.id");
    var prescription = api.prescription(patient, api.generalPractitioner(), "IMAGING", "ULTRASOUND");
    var examination = read(api.examination(patient, prescription, "ULTRASOUND", false), "$.id");

    api.get("/api/medical/imaging/examinations/overdue").andExpect(jsonPath("$[*].id").value(not(hasItem(examination))));
    clock.at(NOW.plusHours(51));
    api.get("/api/medical/imaging/examinations/overdue").andExpect(jsonPath("$[*].id").value(hasItem(examination)));
  }

  @Test
  void shouldNotFindUnknownPrescriptionOrExamination() throws Exception {
    api
      .examination(api.patient(), UUID.randomUUID().toString(), "XRAY", true)
      .andExpect(status().isNotFound())
      .andExpect(jsonPath("$.title").value("Unknown element"));
    api.get("/api/medical/imaging/examinations/" + UUID.randomUUID()).andExpect(status().isNotFound());
  }

  @Test
  void shouldNotPerformWithoutExam() throws Exception {
    api
      .postJson("/api/medical/imaging/examinations", "{\"patientId\":\"%s\",\"prescriptionId\":\"%s\"}".formatted(api.patient(), UUID.randomUUID()))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.title").value("Invalid value"));
  }
}
