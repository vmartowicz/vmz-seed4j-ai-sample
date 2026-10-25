package fr.vmz.seed4j.sample.ai.expert.conference.cfp.infrastructure.primary;

import static fr.vmz.seed4j.sample.ai.expert.conference.ConferenceApi.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.vmz.seed4j.sample.ai.IntegrationTest;
import fr.vmz.seed4j.sample.ai.expert.conference.ConferenceApi;
import fr.vmz.seed4j.sample.ai.expert.conference.ConferenceApi.Speaker;
import fr.vmz.seed4j.sample.ai.expert.conference.ConferenceClock;
import fr.vmz.seed4j.sample.ai.expert.conference.ConferenceClockConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.web.servlet.MockMvc;

@IntegrationTest
@AutoConfigureMockMvc
@Import(ConferenceClockConfiguration.class)
@DirtiesContext(classMode = ClassMode.BEFORE_CLASS)
class SelectionIT {

  @Autowired
  private MockMvc rest;

  @Autowired
  private ConferenceClock clock;

  private ConferenceApi api;

  @BeforeEach
  void loadApi() {
    api = new ConferenceApi(rest, clock);
    clock.at(DURING_CALL_FOR_PAPERS);
  }

  @Test
  @DirtiesContext
  void shouldSelectBestTalksAndReplaceWithdrawalsFromWaitingList() throws Exception {
    var best = api.proposalId("TALK", "BACK", Speaker.newcomer());
    var byVeteran = api.proposalId("TALK", "BACK", new Speaker(unique("veteran"), unique("company"), false, true));
    var byNewcomer = api.proposalId("TALK", "BACK", Speaker.newcomer());
    var unranked = api.proposalId("TALK", "BACK", Speaker.newcomer());
    api.rateThreeTimes(best, 5);
    api.rateThreeTimes(byVeteran, 3);
    api.rateThreeTimes(byNewcomer, 3);
    api.rate(unranked, api.member(unique("company")), 5);
    clock.at(SELECTION);

    api.select(1, 0, 0).andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(4));

    api.get("/api/conference/cfp/proposals/" + best).andExpect(jsonPath("$.status").value("ACCEPTED"));
    api.get("/api/conference/cfp/proposals/" + unranked).andExpect(jsonPath("$.status").value("REJECTED"));
    api.get("/api/conference/cfp/waiting-list?format=TALK").andExpect(jsonPath("$[*].id").value(contains(byNewcomer, byVeteran)));

    api.step(best, "withdrawal").andExpect(status().isOk());

    api.get("/api/conference/cfp/proposals/" + byNewcomer).andExpect(jsonPath("$.status").value("ACCEPTED"));
    api.get("/api/conference/cfp/waiting-list?format=TALK").andExpect(jsonPath("$[*].id").value(contains(byVeteran)));
  }
}
