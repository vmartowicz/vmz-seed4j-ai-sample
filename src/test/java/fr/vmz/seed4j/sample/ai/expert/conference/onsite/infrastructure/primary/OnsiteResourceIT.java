package fr.vmz.seed4j.sample.ai.expert.conference.onsite.infrastructure.primary;

import static fr.vmz.seed4j.sample.ai.expert.conference.ConferenceApi.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.jayway.jsonpath.JsonPath;
import fr.vmz.seed4j.sample.ai.IntegrationTest;
import fr.vmz.seed4j.sample.ai.expert.conference.ConferenceApi;
import fr.vmz.seed4j.sample.ai.expert.conference.ConferenceApi.Speaker;
import fr.vmz.seed4j.sample.ai.expert.conference.ConferenceClock;
import fr.vmz.seed4j.sample.ai.expert.conference.ConferenceClockConfiguration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
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
class OnsiteResourceIT {

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
  void shouldPrintAttendeeBadgeAndReprintItAfterNameChange() throws Exception {
    var jane = unique("jane");
    var ticket = api.ticket(jane);

    var first = badgeOf(
      printBadge(ticket)
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.name").value(jane))
        .andExpect(jsonPath("$.type").value("ATTENDEE"))
        .andExpect(jsonPath("$.ticket").value(ticket))
        .andExpect(jsonPath("$.active").value(true))
    );
    var john = unique("john");
    api.postJson("/api/conference/tickets/" + ticket + "/name-change", "{\"name\":\"%s\",\"email\":\"%s\"}".formatted(john, emailOf(john)));

    printBadge(ticket).andExpect(jsonPath("$.name").value(john));

    api.get("/api/conference/onsite/badges/" + first).andExpect(jsonPath("$.active").value(false));
    enter("AMPHI", first).andExpect(status().isConflict());
  }

  @Test
  void shouldPrintBadgeTypeFromTicket() throws Exception {
    var speaker = Speaker.newcomer();
    api.confirmedTalk("TALK", "CRAFT", speaker);
    var sponsor = emailOf(unique("sponsor"));
    api.paidSponsor("BRONZE", sponsor);

    printBadge(read(api.ticketsOf(speaker.email()), "$[0].number")).andExpect(jsonPath("$.type").value("SPEAKER"));
    printBadge(read(api.ticketsOf(sponsor), "$[0].number")).andExpect(jsonPath("$.type").value("SPONSOR"));
  }

  @Test
  void shouldNotPrintBadgeOfUnknownOrRefundedTicket() throws Exception {
    printBadge("RC-00000000").andExpect(status().isNotFound()).andExpect(jsonPath("$.title").value("Unknown element"));
    var ticket = api.ticket(unique("jane"));
    api.postJson("/api/conference/tickets/" + ticket + "/refund", "{}");

    printBadge(ticket).andExpect(status().isConflict()).andExpect(jsonPath("$.title").value("Business rule violation"));
  }

  @Test
  void shouldCountPeopleMovingBetweenRooms() throws Exception {
    var volunteer = volunteer();
    var amphi = inside("AMPHI");
    var saone = inside("SAONE");

    enter("AMPHI", volunteer).andExpect(status().isOk()).andExpect(jsonPath("$.accepted").value(true));
    assertInside("AMPHI", amphi + 1);
    enter("SAONE", volunteer);
    assertInside("AMPHI", amphi);
    assertInside("SAONE", saone + 1);

    exit(volunteer).andExpect(status().isOk()).andExpect(jsonPath("$.room").value("SAONE")).andExpect(jsonPath("$.exitedAt").isNotEmpty());
    assertInside("SAONE", saone);
    exit(volunteer).andExpect(status().isConflict());
  }

  @Test
  void shouldSendPeopleToLiveStreamWhenRoomIsFull() throws Exception {
    for (int person = inside("CROIX_ROUSSE"); person < 40; person++) {
      enter("CROIX_ROUSSE", volunteer()).andExpect(jsonPath("$.accepted").value(true));
    }

    enter("CROIX_ROUSSE", volunteer())
      .andExpect(jsonPath("$.accepted").value(false))
      .andExpect(jsonPath("$.refusal").value("ROOM_FULL"))
      .andExpect(jsonPath("$.message").value(containsString("live stream")));
  }

  @Test
  void shouldKeepWorkshopsForRegisteredAttendees() throws Exception {
    var speaker = Speaker.newcomer();
    var workshop = api.confirmedTalk("WORKSHOP", "OPS", speaker);
    var block = freeBlock();
    api.place(workshop, "FOURVIERE", block.day(), block.firstSlot()).andExpect(status().isCreated());
    clock.at(JUNE);
    var registered = api.ticket(unique("jane"));
    var unregistered = api.ticket(unique("john"));
    api.postJson("/api/conference/workshops/" + workshop + "/registrations", "{\"ticket\":\"" + registered + "\"}").andExpect(status().isCreated());
    var speakerBadge = badgeOf(printBadge(read(api.ticketsOf(speaker.email()), "$[0].number")));
    var registeredBadge = badgeOf(printBadge(registered));
    var unregisteredBadge = badgeOf(printBadge(unregistered));
    clock.at(startOf(block).plusMinutes(5));

    enter("FOURVIERE", registeredBadge).andExpect(jsonPath("$.accepted").value(true));
    enter("FOURVIERE", speakerBadge).andExpect(jsonPath("$.accepted").value(true));
    enter("FOURVIERE", volunteer()).andExpect(jsonPath("$.accepted").value(true));
    enter("FOURVIERE", unregisteredBadge).andExpect(jsonPath("$.accepted").value(false)).andExpect(jsonPath("$.refusal").value("NOT_REGISTERED"));
  }

  @Test
  void shouldRankTalksByAttendance() throws Exception {
    var block = freeBlock();
    var popular = api.confirmedTalk("TALK", "DATA", Speaker.newcomer());
    var quiet = api.confirmedTalk("TALK", "FRONT", Speaker.newcomer());
    api.place(popular, "AMPHI", block.day(), block.firstSlot()).andExpect(status().isCreated());
    api.place(quiet, "RHONE", block.day(), block.firstSlot()).andExpect(status().isCreated());
    clock.at(startOf(block).plusMinutes(1));
    var jane = volunteer();

    enter("AMPHI", jane);
    enter("AMPHI", volunteer());
    enter("RHONE", volunteer());
    enter("AMPHI", jane);

    api
      .get("/api/conference/onsite/attendance")
      .andExpect(status().isOk())
      .andExpect(jsonPath("$[?(@.talkId == '" + popular + "')].attendees").value(contains(2)))
      .andExpect(jsonPath("$[?(@.talkId == '" + quiet + "')].attendees").value(contains(1)));
  }

  @Test
  void shouldNotEnterWithUnknownBadge() throws Exception {
    enter("AMPHI", UUID.randomUUID().toString()).andExpect(status().isNotFound());
  }

  @Test
  void shouldNotPrintVolunteerBadgeWithoutName() throws Exception {
    api.postJson("/api/conference/onsite/volunteers", "{}").andExpect(status().isBadRequest()).andExpect(jsonPath("$.title").value("Invalid value"));
  }

  private static LocalDateTime startOf(Block block) {
    var starts = new String[] { "09:00", "09:50", "10:40", "11:30", "14:00", "14:50", "15:40", "16:30", "17:20" };

    return block.day().atTime(LocalTime.parse(starts[block.firstSlot() - 1]));
  }

  private ResultActions printBadge(String ticket) throws Exception {
    return api.postJson("/api/conference/onsite/badges", "{\"ticket\":\"" + ticket + "\"}");
  }

  private String volunteer() throws Exception {
    return badgeOf(api.postJson("/api/conference/onsite/volunteers", "{\"name\":\"Volunteer\"}").andExpect(status().isCreated()));
  }

  private ResultActions enter(String room, String badge) throws Exception {
    return api.postJson("/api/conference/onsite/rooms/" + room + "/entries", "{\"badgeId\":\"" + badge + "\"}");
  }

  private ResultActions exit(String badge) throws Exception {
    return api.postJson("/api/conference/onsite/exits", "{\"badgeId\":\"" + badge + "\"}");
  }

  private int inside(String room) throws Exception {
    List<Integer> inside = JsonPath.read(
      api.get("/api/conference/onsite/rooms").andReturn().getResponse().getContentAsString(),
      "$[?(@.room == '" + room + "')].inside"
    );

    return inside.getFirst();
  }

  private void assertInside(String room, int people) throws Exception {
    api.get("/api/conference/onsite/rooms").andExpect(jsonPath("$[?(@.room == '" + room + "')].inside").value(contains(people)));
  }

  private static String badgeOf(ResultActions badge) throws Exception {
    return read(badge, "$.id");
  }
}
