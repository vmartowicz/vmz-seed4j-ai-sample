package fr.vmz.seed4j.sample.ai.expert.conference.sponsoring.infrastructure.primary;

import static fr.vmz.seed4j.sample.ai.expert.conference.ConferenceApi.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.vmz.seed4j.sample.ai.IntegrationTest;
import fr.vmz.seed4j.sample.ai.expert.conference.ConferenceApi;
import fr.vmz.seed4j.sample.ai.expert.conference.ConferenceClock;
import fr.vmz.seed4j.sample.ai.expert.conference.ConferenceClockConfiguration;
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
@Import(ConferenceClockConfiguration.class)
class SponsorshipsResourceIT {

  private static final LocalDateTime SIGNATURE = LocalDateTime.of(2026, 5, 4, 10, 0);

  @Autowired
  private MockMvc rest;

  @Autowired
  private ConferenceClock clock;

  @Autowired
  private UnpaidSponsorshipsJob unpaid;

  private ConferenceApi api;

  @BeforeEach
  void loadApi() {
    api = new ConferenceApi(rest, clock);
    clock.at(SIGNATURE);
  }

  @Test
  void shouldReservePackThenIssueTicketsOnPayment() throws Exception {
    var contact = emailOf(unique("wile"));
    var sponsor = read(
      api
        .sign("SILVER", contact)
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.company").value("Acme"))
        .andExpect(jsonPath("$.contactName").value("Wile Coyote"))
        .andExpect(jsonPath("$.contactEmail").value(contact))
        .andExpect(jsonPath("$.pack").value("SILVER"))
        .andExpect(jsonPath("$.signedOn").value("2026-05-04"))
        .andExpect(jsonPath("$.paymentDeadline").value("2026-06-03"))
        .andExpect(jsonPath("$.status").value("RESERVED")),
      "$.id"
    );
    clock.at(SIGNATURE.plusDays(30));

    api
      .postJson("/api/conference/sponsorships/" + sponsor + "/payment", "{}")
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.status").value("CONFIRMED"))
      .andExpect(jsonPath("$.tickets.length()").value(2));
    api.ticketsOf(contact).andExpect(jsonPath("$[*].type").value(contains("SPONSOR", "SPONSOR")));
    api.get("/api/conference/sponsorships/" + sponsor).andExpect(jsonPath("$.status").value("CONFIRMED"));
  }

  @Test
  void shouldReleaseUnpaidPackAfter30Days() throws Exception {
    var sponsor = read(api.sign("BRONZE", emailOf(unique("wile"))), "$.id");
    clock.at(SIGNATURE.plusDays(31));

    api.postJson("/api/conference/sponsorships/" + sponsor + "/payment", "{}").andExpect(status().isConflict());
    unpaid.release();

    api.get("/api/conference/sponsorships/" + sponsor).andExpect(jsonPath("$.status").value("RELEASED"));
  }

  @Test
  void shouldNotSignWithoutCompany() throws Exception {
    api
      .postJson("/api/conference/sponsorships", "{\"contactName\":\"Wile\",\"contactEmail\":\"wile@acme.com\",\"pack\":\"GOLD\"}")
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.title").value("Invalid value"));
  }

  @Test
  void shouldNotFindUnknownSponsorship() throws Exception {
    api.get("/api/conference/sponsorships/" + UUID.randomUUID()).andExpect(status().isNotFound()).andExpect(jsonPath("$.title").value("Unknown element"));
  }
}
