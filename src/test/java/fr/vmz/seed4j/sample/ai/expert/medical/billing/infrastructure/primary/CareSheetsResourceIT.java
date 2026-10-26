package fr.vmz.seed4j.sample.ai.expert.medical.billing.infrastructure.primary;

import static fr.vmz.seed4j.sample.ai.expert.medical.MedicalApi.*;
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
import fr.vmz.seed4j.sample.ai.expert.medical.billing.application.MedicalBillingApplicationService;
import java.util.UUID;

@IntegrationTest
@AutoConfigureMockMvc
@Import(MedicalClockConfiguration.class)
class CareSheetsResourceIT {

  @Autowired
  private MockMvc rest;

  @Autowired
  private MedicalClock clock;

  @Autowired
  private MedicalBillingApplicationService billing;

  private MedicalApi api;

  @BeforeEach
  void loadApi() {
    api = new MedicalApi(rest);
    clock.at(NOW.plusHours(2));
  }

  @Test
  void shouldSendCareSheetOfChildInTheNameOfParent() throws Exception {
    var parent = api.patient();
    var child = read(api.registerPatient("2016-01-01", "MALE", ",\"insuredId\":\"" + parent + "\""), "$.id");
    var consultation = api.consultation(child, api.generalPractitioner());

    api.close(consultation);

    var sheet = read(
      api
        .get("/api/medical/care-sheets?patient=" + child)
        .andExpect(jsonPath("$[0].insuredId").value(parent))
        .andExpect(jsonPath("$[0].reimbursement").value(9))
        .andExpect(jsonPath("$[0].paidByPatient").value(30)),
      "$[0].id"
    );
    billing.sendPendingCareSheets();
    api.get("/api/medical/care-sheets/" + sheet).andExpect(jsonPath("$.status").value("SENT")).andExpect(jsonPath("$.sentOn").value("2026-06-01"));
  }

  @Test
  void shouldApplyThirdPartyPaymentAndFeeOverrun() throws Exception {
    var specialist = api.doctor("SPECIALIST", "VENISSIEUX", "25", false);
    var patient = api.patient(",\"referringDoctorId\":\"%s\",\"solidarityHealthCover\":true".formatted(specialist));

    api.close(api.consultation(patient, specialist));

    api
      .get("/api/medical/care-sheets?patient=" + patient)
      .andExpect(jsonPath("$[0].baseFee").value(50))
      .andExpect(jsonPath("$[0].feeOverrun").value(25))
      .andExpect(jsonPath("$[0].reimbursement").value(35))
      .andExpect(jsonPath("$[0].thirdPartyPayment").value(true))
      .andExpect(jsonPath("$[0].paidByPatient").value(0));
  }

  @Test
  void shouldMoveAppointmentToChangedInsured() throws Exception {
    var parent = api.patient();
    var child = read(api.registerPatient("2016-01-01", "MALE", ""), "$.id");

    api.postJson("/api/medical/patients/" + child + "/insured", "{\"insuredId\":\"" + parent + "\"}").andExpect(jsonPath("$.insuredId").value(parent));
  }

  @Test
  void shouldNotFindUnknownCareSheet() throws Exception {
    api.get("/api/medical/care-sheets/" + UUID.randomUUID()).andExpect(status().isNotFound()).andExpect(jsonPath("$.title").value("Unknown element"));
  }
}
