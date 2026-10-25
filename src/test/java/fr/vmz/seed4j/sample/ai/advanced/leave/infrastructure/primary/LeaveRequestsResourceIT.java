package fr.vmz.seed4j.sample.ai.advanced.leave.infrastructure.primary;

import static fr.vmz.seed4j.sample.ai.advanced.leave.infrastructure.primary.LeaveApi.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.vmz.seed4j.sample.ai.IntegrationTest;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

@IntegrationTest
@AutoConfigureMockMvc
@Import(FixedClockConfiguration.class)
class LeaveRequestsResourceIT {

  // In-memory requests are shared by the whole Spring context: each test works on its own employee and year
  @Autowired
  private MockMvc rest;

  private LeaveApi api;

  @BeforeEach
  void loadApi() {
    api = new LeaveApi(rest);
  }

  @Nested
  class Request {

    @Test
    void shouldRequestLeave() throws Exception {
      api
        .request(EMMA, "2030-03-04", "2030-03-05")
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").isNotEmpty())
        .andExpect(jsonPath("$.employeeId").value(EMMA))
        .andExpect(jsonPath("$.startDate").value("2030-03-04"))
        .andExpect(jsonPath("$.startHalf").value("MORNING"))
        .andExpect(jsonPath("$.endDate").value("2030-03-05"))
        .andExpect(jsonPath("$.endHalf").value("AFTERNOON"))
        .andExpect(jsonPath("$.days").value(2.0))
        .andExpect(jsonPath("$.status").value("PENDING"))
        .andExpect(jsonPath("$.shortNotice").value(false))
        .andExpect(jsonPath("$.rejectionComment").doesNotExist());
    }

    @Test
    void shouldFlagShortNotice() throws Exception {
      api.request(LUCAS, "2026-03-16", "2026-03-16").andExpect(status().isCreated()).andExpect(jsonPath("$.shortNotice").value(true));
    }

    @Test
    void shouldNotRequestLeaveInThePast() throws Exception {
      api
        .request(EMMA, "2026-03-09", "2026-03-09")
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.title").value("Business rule violation"));
    }

    @Test
    void shouldNotRequestLeaveStartingOnWeekEnd() throws Exception {
      api.request(EMMA, "2030-03-09", "2030-03-11").andExpect(status().isBadRequest()).andExpect(jsonPath("$.title").value("Invalid value"));
    }

    @Test
    void shouldNotRequestLeaveWithoutHalfDay() throws Exception {
      api
        .postAs(EMMA, "/api/leave/requests", "{\"startDate\":\"2030-03-11\",\"endDate\":\"2030-03-11\",\"endHalf\":\"AFTERNOON\"}")
        .andExpect(status().isBadRequest());
    }

    @Test
    void shouldNotRequestLeaveForUnknownEmployee() throws Exception {
      api.request(UUID.randomUUID().toString(), "2030-03-04", "2030-03-04").andExpect(status().isNotFound());
    }

    @Test
    void shouldNotRequestMoreThanTheBalance() throws Exception {
      api.request(HUGO, "2034-01-02", "2034-02-10").andExpect(status().isConflict());
    }

    @Test
    void shouldListOwnRequestsFromLatest() throws Exception {
      api.requestId(CHLOE, "2035-03-05", "2035-03-05");
      api.requestId(CHLOE, "2035-06-04", "2035-06-04");

      api
        .getAs(CHLOE, "/api/leave/requests/mine")
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[*].startDate", contains("2035-06-04", "2035-03-05")));
    }
  }

  @Nested
  class Decision {

    @Test
    void shouldApproveRequest() throws Exception {
      var requestId = api.requestId(PAUL, "2030-03-04", "2030-03-04");

      approve(SOPHIE, requestId).andExpect(status().isOk()).andExpect(jsonPath("$.status").value("APPROVED"));
    }

    @Test
    void shouldNotApproveRequestOfAnotherTeam() throws Exception {
      var requestId = api.requestId(PAUL, "2031-03-03", "2031-03-03");

      approve(MARC, requestId).andExpect(status().isForbidden()).andExpect(jsonPath("$.title").value("Access denied"));
    }

    @Test
    void shouldRejectRequestWithComment() throws Exception {
      var requestId = api.requestId(PAUL, "2032-03-01", "2032-03-01");

      api
        .postAs(SOPHIE, "/api/leave/requests/" + requestId + "/rejection", "{\"comment\":\"Trade show\"}")
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value("REJECTED"))
        .andExpect(jsonPath("$.rejectionComment").value("Trade show"));
    }

    @Test
    void shouldNotApproveFourthSimultaneousLeaveOfTheTeam() throws Exception {
      for (var employee : new String[] { EMMA, LUCAS, LEA }) {
        approve(MARC, api.requestId(employee, "2031-03-03", "2031-03-04")).andExpect(status().isOk());
      }
      var hugosRequest = api.requestId(HUGO, "2031-03-04", "2031-03-05");

      approve(MARC, hugosRequest).andExpect(status().isConflict());
    }

    @Test
    void shouldListRequestsToReview() throws Exception {
      var requestId = api.requestId(LEA, "2036-03-03", "2036-03-03");

      api.getAs(MARC, "/api/leave/requests/to-review").andExpect(status().isOk()).andExpect(jsonPath("$[*].id", hasItem(requestId)));
    }

    @Test
    void shouldNotListRequestsToReviewWithoutBeingManager() throws Exception {
      api.getAs(EMMA, "/api/leave/requests/to-review").andExpect(status().isForbidden());
    }

    @Test
    void shouldNotDecideOnUnknownRequest() throws Exception {
      approve(MARC, UUID.randomUUID().toString()).andExpect(status().isNotFound()).andExpect(jsonPath("$.title").value("Unknown element"));
    }

    private org.springframework.test.web.servlet.ResultActions approve(String manager, String requestId) throws Exception {
      return api.postAs(manager, "/api/leave/requests/" + requestId + "/approval", "");
    }
  }

  @Nested
  class Cancel {

    @Test
    void shouldCancelOwnRequest() throws Exception {
      var requestId = api.requestId(PAUL, "2033-03-07", "2033-03-07");

      api
        .postAs(PAUL, "/api/leave/requests/" + requestId + "/cancellation", "")
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value("CANCELLED"));
    }

    @Test
    void shouldNotCancelRequestOfAnotherEmployee() throws Exception {
      var requestId = api.requestId(PAUL, "2034-03-06", "2034-03-06");

      api.postAs(EMMA, "/api/leave/requests/" + requestId + "/cancellation", "").andExpect(status().isForbidden());
    }
  }
}
