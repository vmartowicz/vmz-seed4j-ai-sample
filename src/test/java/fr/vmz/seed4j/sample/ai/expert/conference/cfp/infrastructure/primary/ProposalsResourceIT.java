package fr.vmz.seed4j.sample.ai.expert.conference.cfp.infrastructure.primary;

import static fr.vmz.seed4j.sample.ai.expert.conference.ConferenceApi.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.vmz.seed4j.sample.ai.IntegrationTest;
import fr.vmz.seed4j.sample.ai.expert.conference.ConferenceApi;
import fr.vmz.seed4j.sample.ai.expert.conference.ConferenceApi.Speaker;
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
class ProposalsResourceIT {

  @Autowired
  private MockMvc rest;

  @Autowired
  private ConferenceClock clock;

  @Autowired
  private ConfirmationDeadlinesJob deadlines;

  private ConferenceApi api;

  @BeforeEach
  void loadApi() {
    api = new ConferenceApi(rest, clock);
    clock.at(DURING_CALL_FOR_PAPERS);
  }

  @Test
  void shouldSubmitProposal() throws Exception {
    var speaker = Speaker.newcomer();

    api
      .submit("TALK", "CRAFT", speaker)
      .andExpect(status().isCreated())
      .andExpect(jsonPath("$.id").isNotEmpty())
      .andExpect(jsonPath("$.title").value("Hexagonal for real"))
      .andExpect(jsonPath("$.summary").value("Ports and adapters"))
      .andExpect(jsonPath("$.format").value("TALK"))
      .andExpect(jsonPath("$.level").value("INTERMEDIATE"))
      .andExpect(jsonPath("$.track").value("CRAFT"))
      .andExpect(jsonPath("$.speakers[0].email").value(speaker.email()))
      .andExpect(jsonPath("$.speakers[0].company").value(speaker.company()))
      .andExpect(jsonPath("$.status").value("SUBMITTED"))
      .andExpect(jsonPath("$.ratings").value(0));
  }

  @Test
  void shouldNotSubmitOutsideCallForPapers() throws Exception {
    clock.at(LocalDateTime.of(2026, 4, 1, 0, 0));

    api.submit("TALK", "CRAFT", Speaker.newcomer()).andExpect(status().isConflict()).andExpect(jsonPath("$.title").value("Business rule violation"));
  }

  @Test
  void shouldNotSubmitMoreThan3ProposalsPerSpeaker() throws Exception {
    var speaker = Speaker.newcomer();
    for (int proposal = 0; proposal < 3; proposal++) {
      api.submit("QUICKIE", "FRONT", speaker).andExpect(status().isCreated());
    }

    api.submit("TALK", "FRONT", Speaker.newcomer(), speaker).andExpect(status().isConflict()).andExpect(jsonPath("$.detail").value(containsString("3 proposals")));
  }

  @Test
  void shouldNotSubmitWithInvalidSpeakerEmail() throws Exception {
    var body = proposal("TALK", "CRAFT", Speaker.newcomer()).replaceFirst("\"email\":\"[^\"]+\"", "\"email\":\"jane@mail\"");

    api.postJson("/api/conference/cfp/proposals", body).andExpect(status().isBadRequest()).andExpect(jsonPath("$.detail").value(containsString("jane@mail")));
  }

  @Test
  void shouldNotSubmitWithoutTitle() throws Exception {
    var body = proposal("TALK", "CRAFT", Speaker.newcomer()).replace("\"title\":\"Hexagonal for real\",", "");

    api.postJson("/api/conference/cfp/proposals", body).andExpect(status().isBadRequest()).andExpect(jsonPath("$.title").value("Invalid value"));
  }

  @Test
  void shouldRateProposal() throws Exception {
    var proposal = api.proposalId("TALK", "DATA", Speaker.newcomer());

    api.rate(proposal, api.member(unique("initech")), 4).andExpect(status().isOk()).andExpect(jsonPath("$.ratings").value(1)).andExpect(jsonPath("$.average").value(4));
  }

  @Test
  void shouldNotRateProposalOfOwnCompany() throws Exception {
    var speaker = Speaker.newcomer();
    var proposal = api.proposalId("TALK", "DATA", speaker);

    api.rate(proposal, api.member(speaker.company()), 4).andExpect(status().isConflict()).andExpect(jsonPath("$.detail").value(containsString("company")));
  }

  @Test
  void shouldNotRateWithUnknownMember() throws Exception {
    var proposal = api.proposalId("TALK", "DATA", Speaker.newcomer());

    api.rate(proposal, UUID.randomUUID().toString(), 4).andExpect(status().isNotFound());
  }

  @Test
  void shouldGiveFreeTicketToConfirmedSpeakers() throws Exception {
    var jane = Speaker.newcomer();
    var john = Speaker.newcomer();

    var proposal = api.confirmedTalk("TALK", "BACK", jane, john);

    api.get("/api/conference/cfp/proposals/" + proposal).andExpect(jsonPath("$.status").value("CONFIRMED"));
    api.ticketsOf(jane.email()).andExpect(jsonPath("$[*].type").value(contains("SPEAKER"))).andExpect(jsonPath("$[0].status").value("VALID"));
    api.ticketsOf(john.email()).andExpect(jsonPath("$[*].type").value(contains("SPEAKER")));
  }

  @Test
  void shouldRefundTicketBoughtBeforeBeingAccepted() throws Exception {
    var jane = Speaker.newcomer();
    api.ticket(jane.name());

    api.confirmedTalk("TALK", "BACK", jane);

    api
      .ticketsOf(jane.email())
      .andExpect(jsonPath("$[?(@.type == 'EARLY_BIRD')].status").value(contains("REFUNDED")))
      .andExpect(jsonPath("$[?(@.type == 'SPEAKER')].status").value(contains("VALID")));
  }

  @Test
  void shouldRevokeSpeakerTicketOnWithdrawal() throws Exception {
    var jane = Speaker.newcomer();
    var proposal = api.confirmedTalk("QUICKIE", "OPS", jane);

    api.step(proposal, "withdrawal").andExpect(status().isOk()).andExpect(jsonPath("$.status").value("WITHDRAWN"));

    api.ticketsOf(jane.email()).andExpect(jsonPath("$[0].status").value("CANCELLED"));
  }

  @Test
  void shouldNotConfirmAfter10Days() throws Exception {
    var proposal = api.acceptedTalk("TALK", "FRONT", Speaker.newcomer());
    clock.at(SELECTION.plusDays(11));

    api.step(proposal, "confirmation").andExpect(status().isConflict());
  }

  @Test
  void shouldExpireUnconfirmedTalks() throws Exception {
    var proposal = api.acceptedTalk("TALK", "FRONT", Speaker.newcomer());
    api.get("/api/conference/cfp/proposals/" + proposal).andExpect(jsonPath("$.confirmationDeadline").value("2026-04-20"));
    clock.at(SELECTION.plusDays(11));

    deadlines.expire();

    api.get("/api/conference/cfp/proposals/" + proposal).andExpect(jsonPath("$.status").value("EXPIRED"));
  }

  @Test
  void shouldConfirmSponsoredTalkOfGoldSponsorOnce() throws Exception {
    var sponsor = api.paidSponsor("GOLD", emailOf(unique("gold")));
    var body = proposal("TALK", "DATA", Speaker.newcomer()).replaceFirst("\\{", "{\"sponsorId\":\"" + sponsor + "\",");
    clock.at(LocalDateTime.of(2026, 9, 1, 10, 0));

    api
      .postJson("/api/conference/cfp/sponsored-proposals", body)
      .andExpect(status().isCreated())
      .andExpect(jsonPath("$.status").value("CONFIRMED"))
      .andExpect(jsonPath("$.sponsorId").value(sponsor));
    api.postJson("/api/conference/cfp/sponsored-proposals", body).andExpect(status().isConflict());
  }

  @Test
  void shouldNotFindUnknownProposal() throws Exception {
    api.get("/api/conference/cfp/proposals/" + UUID.randomUUID()).andExpect(status().isNotFound()).andExpect(jsonPath("$.title").value("Unknown element"));
  }
}
