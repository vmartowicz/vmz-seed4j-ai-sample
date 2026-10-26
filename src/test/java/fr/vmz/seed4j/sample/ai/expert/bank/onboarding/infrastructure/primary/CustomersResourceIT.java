package fr.vmz.seed4j.sample.ai.expert.bank.onboarding.infrastructure.primary;

import static fr.vmz.seed4j.sample.ai.expert.bank.BankApi.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.vmz.seed4j.sample.ai.IntegrationTest;
import fr.vmz.seed4j.sample.ai.expert.bank.BankApi;
import fr.vmz.seed4j.sample.ai.expert.bank.BankClock;
import fr.vmz.seed4j.sample.ai.expert.bank.BankClockConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import fr.vmz.seed4j.sample.ai.expert.bank.onboarding.application.OnboardingApplicationService;
import java.util.UUID;

@IntegrationTest
@AutoConfigureMockMvc
@Import(BankClockConfiguration.class)
class CustomersResourceIT {

  @Autowired
  private MockMvc rest;

  @Autowired
  private BankClock clock;

  @Autowired
  private OnboardingApplicationService onboarding;

  private BankApi api;

  @BeforeEach
  void loadApi() {
    api = new BankApi(rest);
    clock.at(NOW);
  }

  @Test
  void shouldOnboardProspectWithCompleteFile() throws Exception {
    api
      .onboard("1990-05-04", "2500", "2026-05-15", false, null)
      .andExpect(status().isCreated())
      .andExpect(jsonPath("$.id").isNotEmpty())
      .andExpect(jsonPath("$.fullName").value("Jane Doe"))
      .andExpect(jsonPath("$.monthlyIncome").value(2500))
      .andExpect(jsonPath("$.risk").value("LOW"))
      .andExpect(jsonPath("$.customerSince").value("2026-06-01"))
      .andExpect(jsonPath("$.nextReviewOn").value("2029-06-01"))
      .andExpect(jsonPath("$.minor").value(false));
  }

  @Test
  void shouldRatePoliticallyExposedPeopleAsHighRisk() throws Exception {
    api.onboard("1990-05-04", "2500", "2026-05-15", true, null).andExpect(jsonPath("$.risk").value("HIGH")).andExpect(jsonPath("$.nextReviewOn").value("2027-06-01"));
  }

  @Test
  void shouldOnboardMinorWithLegalGuardian() throws Exception {
    api.onboard("2012-01-01", "0", "2026-05-15", false, "John Doe").andExpect(status().isCreated()).andExpect(jsonPath("$.minor").value(true));
    api.onboard("2012-01-01", "0", "2026-05-15", false, null).andExpect(status().isConflict()).andExpect(jsonPath("$.detail").value(containsString("legal guardian")));
  }

  @Test
  void shouldNotOnboardWithOldProofOfAddress() throws Exception {
    api.onboard("1990-05-04", "2500", "2026-02-28", false, null).andExpect(status().isConflict()).andExpect(jsonPath("$.title").value("Business rule violation"));
  }

  @Test
  void shouldNotOnboardWithoutName() throws Exception {
    api
      .postJson("/api/retail-bank/customers", "{\"birthDate\":\"1990-05-04\",\"monthlyIncome\":1,\"identityDocumentExpiry\":\"2030-01-01\",\"proofOfAddressDate\":\"2026-05-15\",\"incomeProofProvided\":true,\"politicallyExposed\":false,\"risk\":\"LOW\"}")
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.title").value("Invalid value"));
  }

  @Test
  void shouldFreezeEveryAccountOfOverdueCustomerUntilReview() throws Exception {
    var late = api.customer("1500");
    var partner = api.customer("1500");
    var own = api.account("CURRENT", late);
    var joint = api.account("CURRENT", partner, late);
    var partnerOnly = api.account("CURRENT", partner);
    clock.at(NOW.plusYears(3).plusDays(1));

    onboarding.freezeOverdueReviews();

    api.get("/api/retail-bank/accounts/" + own).andExpect(jsonPath("$.frozen").value(true)).andExpect(jsonPath("$.freezes").value(contains("REVIEW_OVERDUE")));
    api.get("/api/retail-bank/accounts/" + joint).andExpect(jsonPath("$.frozen").value(true));
    api.get("/api/retail-bank/accounts/" + partnerOnly).andExpect(jsonPath("$.frozen").value(true));

    api.postJson("/api/retail-bank/customers/" + late + "/review", "{\"risk\":\"MEDIUM\"}").andExpect(status().isOk()).andExpect(jsonPath("$.risk").value("MEDIUM"));
    api.get("/api/retail-bank/accounts/" + own).andExpect(jsonPath("$.frozen").value(false));
    api.get("/api/retail-bank/accounts/" + joint).andExpect(jsonPath("$.frozen").value(true));

    api.postJson("/api/retail-bank/customers/" + partner + "/review", "{\"risk\":\"LOW\"}").andExpect(status().isOk());

    api.get("/api/retail-bank/accounts/" + joint).andExpect(jsonPath("$.frozen").value(false));
  }

  @Test
  void shouldViewCustomer() throws Exception {
    var customer = api.customer("1500");

    api.get("/api/retail-bank/customers/" + customer).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(customer));
  }

  @Test
  void shouldNotFindUnknownCustomer() throws Exception {
    api.get("/api/retail-bank/customers/" + UUID.randomUUID()).andExpect(status().isNotFound()).andExpect(jsonPath("$.title").value("Unknown element"));
  }
}
