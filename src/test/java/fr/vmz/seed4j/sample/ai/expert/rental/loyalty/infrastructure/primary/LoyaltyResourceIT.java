package fr.vmz.seed4j.sample.ai.expert.rental.loyalty.infrastructure.primary;

import static fr.vmz.seed4j.sample.ai.expert.rental.RentalApi.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.vmz.seed4j.sample.ai.IntegrationTest;
import fr.vmz.seed4j.sample.ai.expert.rental.RentalApi;
import fr.vmz.seed4j.sample.ai.expert.rental.RentalClock;
import fr.vmz.seed4j.sample.ai.expert.rental.RentalClockConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

@IntegrationTest
@AutoConfigureMockMvc
@Import(RentalClockConfiguration.class)
class LoyaltyResourceIT {

  @Autowired
  private MockMvc rest;

  @Autowired
  private RentalClock clock;

  private RentalApi api;

  @BeforeEach
  void loadApi() {
    api = new RentalApi(rest);
    clock.at(NOW);
  }

  @Test
  void shouldShowMemberWithoutPoints() throws Exception {
    var email = uniqueEmail();

    api
      .get("/api/rental/loyalty/" + email)
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.customer").value(email))
      .andExpect(jsonPath("$.balance").value(0))
      .andExpect(jsonPath("$.earnedInLast12Months").value(0))
      .andExpect(jsonPath("$.status").value("MEMBER"));
  }
}
