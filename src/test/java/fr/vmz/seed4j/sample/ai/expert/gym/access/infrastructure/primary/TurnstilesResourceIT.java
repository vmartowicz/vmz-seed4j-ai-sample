package fr.vmz.seed4j.sample.ai.expert.gym.access.infrastructure.primary;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.vmz.seed4j.sample.ai.IntegrationTest;
import fr.vmz.seed4j.sample.ai.expert.gym.GymApi;
import fr.vmz.seed4j.sample.ai.expert.gym.GymClock;
import fr.vmz.seed4j.sample.ai.expert.gym.GymClockConfiguration;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@IntegrationTest
@AutoConfigureMockMvc
@Import(GymClockConfiguration.class)
class TurnstilesResourceIT {

  private static final LocalDateTime NOW = LocalDateTime.of(2026, 3, 10, 18, 0);

  @Autowired
  private MockMvc rest;

  @Autowired
  private GymClock clock;

  private GymApi api;

  @BeforeEach
  void loadApi() {
    api = new GymApi(rest);
    clock.at(NOW);
  }

  @Test
  void shouldLetActiveMemberInAndOut() throws Exception {
    var member = api.activeMember("ESSENTIAL", "CONFLUENCE");
    var before = Integer.parseInt(GymApi.read(occupancy("CONFLUENCE"), "$.inside"));

    api.enter("CONFLUENCE", member.badge()).andExpect(status().isOk()).andExpect(jsonPath("$.accepted").value(true));

    occupancy("CONFLUENCE").andExpect(jsonPath("$.inside").value(before + 1)).andExpect(jsonPath("$.capacity").value(100));
    exit(member.badge())
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.gym").value("CONFLUENCE"))
      .andExpect(jsonPath("$.enteredAt").value("2026-03-10T18:00:00"))
      .andExpect(jsonPath("$.exitedAt").value("2026-03-10T18:00:00"));
    occupancy("CONFLUENCE").andExpect(jsonPath("$.inside").value(before));
  }

  @Test
  void shouldRefuseMemberAwaitingFirstPayment() throws Exception {
    var member = api.member("PREMIUM", "PART_DIEU");

    api
      .enter("PART_DIEU", member.badge())
      .andExpect(jsonPath("$.accepted").value(false))
      .andExpect(jsonPath("$.refusal").value("MEMBERSHIP_INACTIVE"))
      .andExpect(jsonPath("$.message").value("Please go to the front desk"));
  }

  @Test
  void shouldLetEssentialMembersOnlyIntoTheirHomeGym() throws Exception {
    var essential = api.activeMember("ESSENTIAL", "CONFLUENCE");
    var premium = api.activeMember("PREMIUM", "CONFLUENCE");

    api.enter("ECULLY", essential.badge()).andExpect(jsonPath("$.refusal").value("GYM_NOT_INCLUDED"));
    api.enter("ECULLY", premium.badge()).andExpect(jsonPath("$.accepted").value(true));
  }

  @Test
  void shouldRefuseSecondEntryWithinFourHoursWithoutExit() throws Exception {
    var member = api.activeMember("PREMIUM", "PART_DIEU");
    api.enter("PART_DIEU", member.badge()).andExpect(jsonPath("$.accepted").value(true));

    api.enter("VILLEURBANNE", member.badge()).andExpect(jsonPath("$.refusal").value("ALREADY_INSIDE"));
    clock.at(NOW.plusHours(4));
    api.enter("VILLEURBANNE", member.badge()).andExpect(jsonPath("$.accepted").value(true));
  }

  @Test
  void shouldRefuseSuspendedMember() throws Exception {
    var member = api.activeMember("ESSENTIAL", "ECULLY");
    api.postJson("/api/gym/memberships/" + member.id() + "/suspension", "{\"start\":\"2026-03-10\",\"months\":1}");

    api.enter("ECULLY", member.badge()).andExpect(jsonPath("$.refusal").value("SUSPENDED"));
  }

  @Test
  void shouldNotEnterWithUnknownBadge() throws Exception {
    api.enter("ECULLY", "FC-00000000").andExpect(status().isNotFound()).andExpect(jsonPath("$.title").value("Unknown element"));
  }

  @Test
  void shouldNotExitWithoutEntering() throws Exception {
    var member = api.activeMember("ESSENTIAL", "ECULLY");

    exit(member.badge()).andExpect(status().isConflict()).andExpect(jsonPath("$.title").value("Business rule violation"));
  }

  @Test
  void shouldNotEnterWithoutBadge() throws Exception {
    api.postJson("/api/gym/access/gyms/ECULLY/entries", "{}").andExpect(status().isBadRequest());
  }

  private ResultActions occupancy(String gym) throws Exception {
    return api.get("/api/gym/access/gyms/" + gym + "/occupancy");
  }

  private ResultActions exit(String badge) throws Exception {
    return api.postJson("/api/gym/access/exits", "{\"badge\":\"" + badge + "\"}");
  }
}
