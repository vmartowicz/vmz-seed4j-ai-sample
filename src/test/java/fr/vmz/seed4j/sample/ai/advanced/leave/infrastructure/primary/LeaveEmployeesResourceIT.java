package fr.vmz.seed4j.sample.ai.advanced.leave.infrastructure.primary;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.vmz.seed4j.sample.ai.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

@IntegrationTest
@AutoConfigureMockMvc
@Import(FixedClockConfiguration.class)
class LeaveEmployeesResourceIT {

  @Autowired
  private MockMvc rest;

  @Test
  void shouldListEmployees() throws Exception {
    rest
      .perform(get("/api/leave/employees"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$", hasSize(9)))
      .andExpect(jsonPath("$[0].id").value(LeaveApi.MARC))
      .andExpect(jsonPath("$[0].name").value("Marc"))
      .andExpect(jsonPath("$[0].email").value("marc@company.com"))
      .andExpect(jsonPath("$[0].team").value("Development"))
      .andExpect(jsonPath("$[0].role").value("MANAGER"));
  }
}
