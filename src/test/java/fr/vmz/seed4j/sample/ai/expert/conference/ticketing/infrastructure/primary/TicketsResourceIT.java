package fr.vmz.seed4j.sample.ai.expert.conference.ticketing.infrastructure.primary;

import static fr.vmz.seed4j.sample.ai.expert.conference.ConferenceApi.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.vmz.seed4j.sample.ai.IntegrationTest;
import fr.vmz.seed4j.sample.ai.expert.conference.ConferenceApi;
import fr.vmz.seed4j.sample.ai.expert.conference.ConferenceApi.Speaker;
import fr.vmz.seed4j.sample.ai.expert.conference.ConferenceClock;
import fr.vmz.seed4j.sample.ai.expert.conference.ConferenceClockConfiguration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@IntegrationTest
@AutoConfigureMockMvc
@Import(ConferenceClockConfiguration.class)
class TicketsResourceIT {

  private static final LocalDateTime JUNE = LocalDateTime.of(2026, 6, 1, 10, 0);

  @Autowired
  private MockMvc rest;

  @Autowired
  private ConferenceClock clock;

  private ConferenceApi api;

  @BeforeEach
  void loadApi() {
    api = new ConferenceApi(rest, clock);
    clock.at(JUNE);
  }

  @Test
  void shouldSellEarlyBirdTicketsUntilApril30() throws Exception {
    clock.at(LocalDateTime.of(2026, 4, 30, 23, 0));
    var jane = unique("jane");

    api
      .order(jane)
      .andExpect(status().isCreated())
      .andExpect(jsonPath("$.id").isNotEmpty())
      .andExpect(jsonPath("$.buyer").value("buyer@company.com"))
      .andExpect(jsonPath("$.total").value(249))
      .andExpect(jsonPath("$.tickets[0].number").value(matchesPattern("RC-[0-9A-F]{8}")))
      .andExpect(jsonPath("$.tickets[0].type").value("EARLY_BIRD"))
      .andExpect(jsonPath("$.tickets[0].holderName").value(jane))
      .andExpect(jsonPath("$.tickets[0].holderEmail").value(emailOf(jane)))
      .andExpect(jsonPath("$.tickets[0].status").value("VALID"));
  }

  @Test
  void shouldDiscountOrdersOf5TicketsWithStudentPrice() throws Exception {
    var attendees = IntStream.range(0, 4).mapToObj(index -> attendee(unique("dev"), false)).collect(Collectors.joining(","));
    var body = "{\"buyer\":\"buyer@company.com\",\"attendees\":[" + attendees + "," + attendee(unique("student"), true) + "]}";

    api
      .postJson("/api/conference/tickets/orders", body)
      .andExpect(status().isCreated())
      .andExpect(jsonPath("$.tickets[*].type").value(contains("STANDARD", "STANDARD", "STANDARD", "STANDARD", "STUDENT")))
      .andExpect(jsonPath("$.tickets[4].price").value(89.1))
      .andExpect(jsonPath("$.total").value(1345.5));
  }

  @Test
  void shouldRenameTicketUntil7DaysBeforeConference() throws Exception {
    var ticket = api.ticket(unique("jane"));
    var john = unique("john");
    clock.at(LocalDateTime.of(2026, 10, 8, 18, 0));

    rename(ticket, john).andExpect(status().isOk()).andExpect(jsonPath("$.holderName").value(john)).andExpect(jsonPath("$.holderEmail").value(emailOf(john)));
    clock.at(LocalDateTime.of(2026, 10, 9, 8, 0));
    rename(ticket, unique("jim")).andExpect(status().isConflict());
  }

  @Test
  void shouldRefundTicketUntil30DaysBeforeConference() throws Exception {
    var refunded = api.ticket(unique("jane"));
    var kept = api.ticket(unique("john"));
    clock.at(LocalDateTime.of(2026, 9, 15, 18, 0));

    refund(refunded).andExpect(status().isOk()).andExpect(jsonPath("$.status").value("REFUNDED"));
    clock.at(LocalDateTime.of(2026, 9, 16, 8, 0));
    refund(kept).andExpect(status().isConflict()).andExpect(jsonPath("$.detail").value(containsString("30 days")));
  }

  @Test
  void shouldNotRefundSponsorTicket() throws Exception {
    var contact = emailOf(unique("sponsor"));
    api.paidSponsor("BRONZE", contact);

    refund(read(api.ticketsOf(contact), "$[0].number")).andExpect(status().isConflict());
  }

  @Test
  void shouldRegisterToWorkshopWithWaitingList() throws Exception {
    var workshop = api.confirmedTalk("WORKSHOP", "OPS", Speaker.newcomer());
    clock.at(JUNE);
    var attendees = IntStream.range(0, 31).mapToObj(index -> attendee(unique("dev"), false)).collect(Collectors.joining(","));
    var order = api.postJson("/api/conference/tickets/orders", "{\"buyer\":\"buyer@company.com\",\"attendees\":[" + attendees + "]}");
    var tickets = new ArrayList<String>();
    for (int index = 0; index < 31; index++) {
      tickets.add(read(order, "$.tickets[" + index + "].number"));
    }
    for (int index = 0; index < 30; index++) {
      register(workshop, tickets.get(index)).andExpect(status().isCreated()).andExpect(jsonPath("$.status").value("REGISTERED"));
    }

    register(workshop, tickets.get(30)).andExpect(jsonPath("$.status").value("WAITING"));
    register(workshop, tickets.get(0)).andExpect(status().isConflict());
    api.postJson("/api/conference/workshops/" + workshop + "/registrations/" + tickets.get(0) + "/cancellation", "{}").andExpect(status().isOk());

    api
      .get("/api/conference/workshops/" + workshop + "/registrations")
      .andExpect(jsonPath("$[?(@.ticket == '" + tickets.get(30) + "')].status").value(contains("REGISTERED")))
      .andExpect(jsonPath("$[?(@.ticket == '" + tickets.get(0) + "')].status").value(contains("CANCELLED")));
  }

  @Test
  void shouldNotRegisterToUnknownWorkshop() throws Exception {
    register(UUID.randomUUID().toString(), api.ticket(unique("jane"))).andExpect(status().isNotFound());
  }

  @Test
  void shouldNotRegisterRefundedTicket() throws Exception {
    var workshop = api.confirmedTalk("WORKSHOP", "DATA", Speaker.newcomer());
    clock.at(JUNE);
    var ticket = api.ticket(unique("jane"));
    refund(ticket);

    register(workshop, ticket).andExpect(status().isConflict());
  }

  @Test
  void shouldViewTicket() throws Exception {
    var ticket = api.ticket(unique("jane"));

    api.get("/api/conference/tickets/" + ticket.toLowerCase()).andExpect(status().isOk()).andExpect(jsonPath("$.number").value(ticket));
  }

  @Test
  void shouldNotFindUnknownTicket() throws Exception {
    api.get("/api/conference/tickets/RC-00000000").andExpect(status().isNotFound()).andExpect(jsonPath("$.title").value("Unknown element"));
  }

  @Test
  void shouldNotOrderWithoutAttendee() throws Exception {
    api.postJson("/api/conference/tickets/orders", "{\"buyer\":\"buyer@company.com\"}").andExpect(status().isBadRequest());
  }

  private ResultActions rename(String ticket, String name) throws Exception {
    return api.postJson("/api/conference/tickets/" + ticket + "/name-change", "{\"name\":\"%s\",\"email\":\"%s\"}".formatted(name, emailOf(name)));
  }

  private ResultActions refund(String ticket) throws Exception {
    return api.postJson("/api/conference/tickets/" + ticket + "/refund", "{}");
  }

  private ResultActions register(String workshop, String ticket) throws Exception {
    return api.postJson("/api/conference/workshops/" + workshop + "/registrations", "{\"ticket\":\"" + ticket + "\"}");
  }
}
