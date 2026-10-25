package fr.vmz.seed4j.sample.ai.expert.gym.membership.infrastructure.primary;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.vmz.seed4j.sample.ai.IntegrationTest;
import fr.vmz.seed4j.sample.ai.expert.gym.GymApi;
import fr.vmz.seed4j.sample.ai.expert.gym.GymClock;
import fr.vmz.seed4j.sample.ai.expert.gym.GymClockConfiguration;
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
@Import(GymClockConfiguration.class)
class MembershipsResourceIT {

  @Autowired
  private MockMvc rest;

  @Autowired
  private GymClock clock;

  private GymApi api;

  @BeforeEach
  void loadApi() {
    api = new GymApi(rest);
    clock.at(LocalDateTime.of(2026, 3, 10, 10, 0));
  }

  @Test
  void shouldSubscribeWithBadgeAndFirstInvoice() throws Exception {
    api
      .subscribe("ESSENTIAL", "CONFLUENCE", true, "1990-05-04", false)
      .andExpect(status().isCreated())
      .andExpect(jsonPath("$.membership.id").isNotEmpty())
      .andExpect(jsonPath("$.membership.fullName").value("Jane Doe"))
      .andExpect(jsonPath("$.membership.plan").value("ESSENTIAL"))
      .andExpect(jsonPath("$.membership.homeGym").value("CONFLUENCE"))
      .andExpect(jsonPath("$.membership.committed").value(true))
      .andExpect(jsonPath("$.membership.monthlyFee").value(29.9))
      .andExpect(jsonPath("$.membership.subscribedOn").value("2026-03-10"))
      .andExpect(jsonPath("$.membership.commitmentEnd").value("2027-03-10"))
      .andExpect(jsonPath("$.membership.status").value("PENDING_PAYMENT"))
      .andExpect(jsonPath("$.badge").value(matchesPattern("FC-[0-9A-F]{8}")))
      .andExpect(jsonPath("$.firstInvoice.id").isNotEmpty())
      .andExpect(jsonPath("$.firstInvoice.amount").value(60.22));
  }

  @Test
  void shouldActivateMembershipOnceFirstInvoiceIsPaid() throws Exception {
    var member = api.member("PREMIUM", "PART_DIEU");

    api.payByCard(member.firstInvoice()).andExpect(status().isOk());

    api.get("/api/gym/memberships/" + member.id()).andExpect(status().isOk()).andExpect(jsonPath("$.status").value("ACTIVE"));
  }

  @Test
  void shouldNotSubscribeUnder16() throws Exception {
    api
      .subscribe("ESSENTIAL", "ECULLY", true, "2010-03-11", true)
      .andExpect(status().isConflict())
      .andExpect(jsonPath("$.title").value("Business rule violation"));
  }

  @Test
  void shouldNotSubscribeMinorWithoutParentalConsent() throws Exception {
    api.subscribe("ESSENTIAL", "ECULLY", true, "2009-03-10", false).andExpect(status().isConflict());
  }

  @Test
  void shouldNotSubscribeWithoutName() throws Exception {
    api
      .postJson("/api/gym/memberships", "{\"birthDate\":\"1990-01-01\",\"parentalConsent\":false,\"studentCard\":false,\"plan\":\"PREMIUM\",\"committed\":true,\"homeGym\":\"ECULLY\"}")
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.title").value("Invalid value"));
  }

  @Test
  void shouldSuspendMembershipAndExtendCommitment() throws Exception {
    var member = api.activeMember("ESSENTIAL", "ECULLY");

    api
      .postJson("/api/gym/memberships/" + member.id() + "/suspension", "{\"start\":\"2026-04-01\",\"months\":2}")
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.status").value("ACTIVE"))
      .andExpect(jsonPath("$.commitmentEnd").value("2027-05-10"));
    clock.at(LocalDateTime.of(2026, 4, 1, 8, 0));

    api.get("/api/gym/memberships/" + member.id()).andExpect(jsonPath("$.status").value("SUSPENDED"));
  }

  @Test
  void shouldSuspendOnlyOncePerYear() throws Exception {
    var member = api.activeMember("ESSENTIAL", "ECULLY");
    api.postJson("/api/gym/memberships/" + member.id() + "/suspension", "{\"start\":\"2026-04-01\",\"months\":1}").andExpect(status().isOk());

    api
      .postJson("/api/gym/memberships/" + member.id() + "/suspension", "{\"start\":\"2026-10-01\",\"months\":1}")
      .andExpect(status().isConflict())
      .andExpect(jsonPath("$.detail").value(containsString("2026")));
  }

  @Test
  void shouldNotSuspendMembershipAwaitingFirstPayment() throws Exception {
    var member = api.member("ESSENTIAL", "ECULLY");

    api.postJson("/api/gym/memberships/" + member.id() + "/suspension", "{\"start\":\"2026-04-01\",\"months\":1}").andExpect(status().isConflict());
  }

  @Test
  void shouldNotViewUnknownMembership() throws Exception {
    api
      .get("/api/gym/memberships/" + UUID.randomUUID())
      .andExpect(status().isNotFound())
      .andExpect(jsonPath("$.title").value("Unknown element"));
  }
}
