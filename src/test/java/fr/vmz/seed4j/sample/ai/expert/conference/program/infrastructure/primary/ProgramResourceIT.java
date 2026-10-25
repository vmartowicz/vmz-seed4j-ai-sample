package fr.vmz.seed4j.sample.ai.expert.conference.program.infrastructure.primary;

import static fr.vmz.seed4j.sample.ai.expert.conference.ConferenceApi.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.vmz.seed4j.sample.ai.IntegrationTest;
import fr.vmz.seed4j.sample.ai.expert.conference.ConferenceApi;
import fr.vmz.seed4j.sample.ai.expert.conference.ConferenceApi.Speaker;
import fr.vmz.seed4j.sample.ai.expert.conference.ConferenceClock;
import fr.vmz.seed4j.sample.ai.expert.conference.ConferenceClockConfiguration;
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
class ProgramResourceIT {

  @Autowired
  private MockMvc rest;

  @Autowired
  private ConferenceClock clock;

  private ConferenceApi api;

  @BeforeEach
  void loadApi() {
    api = new ConferenceApi(rest, clock);
  }

  @Test
  void shouldPlaceConfirmedTalkUntilItIsWithdrawn() throws Exception {
    var speaker = Speaker.newcomer();
    var talk = api.confirmedTalk("TALK", "BACK", speaker);
    var block = freeBlock();

    api
      .place(talk, "AMPHI", block.day(), block.firstSlot())
      .andExpect(status().isCreated())
      .andExpect(jsonPath("$.talkId").value(talk))
      .andExpect(jsonPath("$.title").value("Hexagonal for real"))
      .andExpect(jsonPath("$.format").value("TALK"))
      .andExpect(jsonPath("$.track").value("BACK"))
      .andExpect(jsonPath("$.speakers").value(contains(speaker.email())))
      .andExpect(jsonPath("$.room").value("AMPHI"));
    api.get("/api/conference/program/days/" + block.day()).andExpect(jsonPath("$[*].talkId").value(hasItem(talk)));

    api.step(talk, "withdrawal").andExpect(status().isOk());

    api.get("/api/conference/program/days/" + block.day()).andExpect(jsonPath("$[*].talkId").value(not(hasItem(talk))));
  }

  @Test
  void shouldPlaceWorkshopsOn2SlotsInFourviereOnly() throws Exception {
    var workshop = api.confirmedTalk("WORKSHOP", "OPS", Speaker.newcomer());
    var block = freeBlock();

    api.place(workshop, "SAONE", block.day(), block.firstSlot()).andExpect(status().isConflict());
    api
      .place(workshop, "FOURVIERE", block.day(), block.firstSlot())
      .andExpect(status().isCreated())
      .andExpect(jsonPath("$.start").value(startsWith(block.day().toString())))
      .andExpect(jsonPath("$.end").value(containsString("T" + endOf(block.firstSlot() + 1))));
  }

  @Test
  void shouldKeepRoomsTracksAndSpeakersFree() throws Exception {
    var jane = Speaker.newcomer();
    var block = freeBlock();
    var placed = api.confirmedTalk("TALK", "BACK", jane);
    api.place(placed, "AMPHI", block.day(), block.firstSlot()).andExpect(status().isCreated());

    api.place(api.confirmedTalk("TALK", "FRONT", Speaker.newcomer()), "AMPHI", block.day(), block.firstSlot()).andExpect(status().isConflict());
    api.place(api.confirmedTalk("TALK", "BACK", Speaker.newcomer()), "SAONE", block.day(), block.firstSlot()).andExpect(status().isConflict());
    api
      .place(api.confirmedTalk("QUICKIE", "DATA", jane), "RHONE", block.day(), block.firstSlot())
      .andExpect(status().isConflict())
      .andExpect(jsonPath("$.detail").value(containsString(jane.email())));
    api.place(api.confirmedTalk("TALK", "FRONT", Speaker.newcomer()), "SAONE", block.day(), block.firstSlot()).andExpect(status().isCreated());
    api.place(placed, "RHONE", block.day(), block.firstSlot() + 1).andExpect(status().isConflict());
  }

  @Test
  void shouldNotScheduleSpeakersFromAbroadBefore11OnFirstDay() throws Exception {
    var talk = api.confirmedTalk("TALK", "CRAFT", Speaker.foreigner());

    api.place(talk, "SAONE", FIRST_DAY, 3).andExpect(status().isConflict()).andExpect(jsonPath("$.detail").value(containsString("11:00")));
    api.place(talk, "SAONE", FIRST_DAY, 4).andExpect(status().isCreated());
  }

  @Test
  void shouldKeepFirstSlotForKeynote() throws Exception {
    var talk = api.confirmedTalk("TALK", "CRAFT", Speaker.newcomer());

    api.place(talk, "SAONE", FIRST_DAY, 1).andExpect(status().isConflict()).andExpect(jsonPath("$.detail").value(containsString("keynote")));
  }

  @Test
  void shouldNotPlaceOutsideConferenceSlots() throws Exception {
    var talk = api.confirmedTalk("TALK", "CRAFT", Speaker.newcomer());

    api.place(talk, "SAONE", FIRST_DAY, 10).andExpect(status().isBadRequest()).andExpect(jsonPath("$.title").value("Invalid value"));
  }

  @Test
  void shouldNotPlaceSessionWithoutRoom() throws Exception {
    api
      .postJson("/api/conference/program/sessions", "{\"talkId\":\"" + UUID.randomUUID() + "\",\"day\":\"2026-10-16\",\"slot\":2}")
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.detail").value(containsString("room")));
  }

  @Test
  void shouldNotPlaceTalkNotConfirmed() throws Exception {
    var accepted = api.acceptedTalk("TALK", "CRAFT", Speaker.newcomer());

    api.place(accepted, "SAONE", SECOND_DAY, 9).andExpect(status().isNotFound());
    api.place(UUID.randomUUID().toString(), "SAONE", SECOND_DAY, 9).andExpect(status().isNotFound()).andExpect(jsonPath("$.title").value("Unknown element"));
  }

  private static String endOf(int slot) {
    return new String[] { "09:40", "10:30", "11:20", "12:10", "14:40", "15:30", "16:20", "17:10", "18:00" }[slot - 1];
  }
}
