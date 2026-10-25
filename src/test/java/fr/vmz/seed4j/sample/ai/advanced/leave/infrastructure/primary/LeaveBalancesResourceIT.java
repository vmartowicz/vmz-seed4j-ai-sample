package fr.vmz.seed4j.sample.ai.advanced.leave.infrastructure.primary;

import static fr.vmz.seed4j.sample.ai.advanced.leave.infrastructure.primary.LeaveApi.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.vmz.seed4j.sample.ai.IntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

@IntegrationTest
@AutoConfigureMockMvc
@Import(FixedClockConfiguration.class)
class LeaveBalancesResourceIT {

  // In-memory data is shared by the whole Spring context: each test works on its own employee and year
  @Autowired
  private MockMvc rest;

  private LeaveApi api;

  @BeforeEach
  void loadApi() {
    api = new LeaveApi(rest);
  }

  @Test
  void shouldViewOwnBalanceOfAYear() throws Exception {
    api.requestId(SOPHIE, "2037-03-02", "2037-03-03");

    api
      .getAs(SOPHIE, "/api/leave/balance?year=2037")
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.year").value(2037))
      .andExpect(jsonPath("$.entitlement").value(25.0))
      .andExpect(jsonPath("$.approved").value(0.0))
      .andExpect(jsonPath("$.pending").value(2.0))
      .andExpect(jsonPath("$.remaining").value(23.0));
  }

  @Test
  void shouldViewBalanceOfTheCurrentYear() throws Exception {
    api.getAs(INES, "/api/leave/balance").andExpect(status().isOk()).andExpect(jsonPath("$.year").value(2026));
  }

  @Test
  void shouldChangeEntitlementAsHumanResources() throws Exception {
    api
      .putAs(INES, "/api/leave/balances/" + CHLOE + "/2038", "{\"days\":27.5}")
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.entitlement").value(27.5))
      .andExpect(jsonPath("$.remaining").value(27.5));
  }

  @Test
  void shouldNotChangeEntitlementWithoutBeingHumanResources() throws Exception {
    api.putAs(MARC, "/api/leave/balances/" + CHLOE + "/2038", "{\"days\":30}").andExpect(status().isForbidden());
  }

  @Test
  void shouldNotChangeEntitlementToPartialHalfDays() throws Exception {
    api
      .putAs(INES, "/api/leave/balances/" + CHLOE + "/2038", "{\"days\":0.3}")
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.title").value("Invalid value"));
  }

  @Test
  void shouldNotChangeEntitlementBelowConsumedDays() throws Exception {
    api.requestId(SOPHIE, "2039-03-07", "2039-03-08");

    api.putAs(INES, "/api/leave/balances/" + SOPHIE + "/2039", "{\"days\":1}").andExpect(status().isConflict());
  }
}
