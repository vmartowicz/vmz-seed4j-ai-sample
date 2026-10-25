package fr.vmz.seed4j.sample.ai.expert.conference.sponsoring.infrastructure.primary;

import static fr.vmz.seed4j.sample.ai.expert.conference.ConferenceApi.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.vmz.seed4j.sample.ai.IntegrationTest;
import fr.vmz.seed4j.sample.ai.expert.conference.ConferenceApi;
import fr.vmz.seed4j.sample.ai.expert.conference.ConferenceClock;
import fr.vmz.seed4j.sample.ai.expert.conference.ConferenceClockConfiguration;
import java.time.LocalDateTime;
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
class SponsorshipQuotasIT {

  @Autowired
  private MockMvc rest;

  @Autowired
  private ConferenceClock clock;

  @Autowired
  private UnpaidSponsorshipsJob unpaid;

  @Test
  @DirtiesContext
  void shouldSellAtMost3GoldPacksUntilOneIsReleased() throws Exception {
    var api = new ConferenceApi(rest, clock);
    clock.at(LocalDateTime.of(2026, 5, 4, 10, 0));
    var unpaidGold = read(api.sign("GOLD", "first@acme.com"), "$.id");
    api.paidSponsor("GOLD", "second@acme.com");
    api.sign("GOLD", "third@acme.com").andExpect(status().isCreated());

    api.sign("GOLD", "fourth@acme.com").andExpect(status().isConflict()).andExpect(jsonPath("$.detail").value(containsString("GOLD")));

    clock.at(LocalDateTime.of(2026, 6, 4, 10, 0));
    unpaid.release();
    api.get("/api/conference/sponsorships/" + unpaidGold).andExpect(jsonPath("$.status").value("RELEASED"));
    api.sign("GOLD", "fourth@acme.com").andExpect(status().isCreated());
  }
}
