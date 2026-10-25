package fr.vmz.seed4j.sample.ai.expert.conference;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.jayway.jsonpath.JsonPath;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

public final class ConferenceApi {

  public static final LocalDateTime DURING_CALL_FOR_PAPERS = LocalDateTime.of(2026, 3, 1, 10, 0);
  public static final LocalDateTime SELECTION = LocalDateTime.of(2026, 4, 10, 10, 0);
  public static final LocalDate FIRST_DAY = LocalDate.of(2026, 10, 15);
  public static final LocalDate SECOND_DAY = LocalDate.of(2026, 10, 16);

  private static final List<Block> BLOCKS = List.of(
    new Block(FIRST_DAY, 5),
    new Block(FIRST_DAY, 7),
    new Block(SECOND_DAY, 1),
    new Block(SECOND_DAY, 3),
    new Block(SECOND_DAY, 5),
    new Block(SECOND_DAY, 7)
  );
  private static final AtomicInteger USED_BLOCKS = new AtomicInteger();

  private final MockMvc rest;
  private final ConferenceClock clock;

  public ConferenceApi(MockMvc rest, ConferenceClock clock) {
    this.rest = rest;
    this.clock = clock;
  }

  public ResultActions submit(String format, String track, Speaker... speakers) throws Exception {
    return postJson("/api/conference/cfp/proposals", proposal(format, track, speakers));
  }

  public String proposalId(String format, String track, Speaker... speakers) throws Exception {
    return read(submit(format, track, speakers).andExpect(status().isCreated()), "$.id");
  }

  public String member(String company) throws Exception {
    var member = postJson("/api/conference/cfp/committee-members", "{\"name\":\"Member of " + company + "\",\"company\":\"" + company + "\"}");

    return read(member.andExpect(status().isCreated()), "$.id");
  }

  public ResultActions rate(String proposal, String member, int score) throws Exception {
    return postJson("/api/conference/cfp/proposals/" + proposal + "/ratings", "{\"memberId\":\"" + member + "\",\"score\":" + score + "}");
  }

  public void rateThreeTimes(String proposal, int score) throws Exception {
    for (int rating = 0; rating < 3; rating++) {
      rate(proposal, member("company-" + UUID.randomUUID()), score).andExpect(status().isOk());
    }
  }

  public ResultActions select(int talks, int quickies, int workshops) throws Exception {
    return postJson("/api/conference/cfp/selection", "{\"talks\":%d,\"quickies\":%d,\"workshops\":%d}".formatted(talks, quickies, workshops));
  }

  public ResultActions step(String proposal, String step) throws Exception {
    return postJson("/api/conference/cfp/proposals/" + proposal + "/" + step, "{}");
  }

  public String acceptedTalk(String format, String track, Speaker... speakers) throws Exception {
    clock.at(DURING_CALL_FOR_PAPERS);
    var proposal = proposalId(format, track, speakers);
    rateThreeTimes(proposal, 4);
    clock.at(SELECTION);
    select(1000, 1000, 1000).andExpect(status().isOk());

    return proposal;
  }

  public String confirmedTalk(String format, String track, Speaker... speakers) throws Exception {
    var proposal = acceptedTalk(format, track, speakers);
    step(proposal, "confirmation").andExpect(status().isOk());

    return proposal;
  }

  public ResultActions order(String... attendees) throws Exception {
    var body = Stream.of(attendees).map(name -> attendee(name, false)).collect(Collectors.joining(","));

    return postJson("/api/conference/tickets/orders", "{\"buyer\":\"buyer@company.com\",\"attendees\":[" + body + "]}");
  }

  public String ticket(String attendee) throws Exception {
    return read(order(attendee).andExpect(status().isCreated()), "$.tickets[0].number");
  }

  public ResultActions ticketsOf(String email) throws Exception {
    return get("/api/conference/tickets?holder=" + email);
  }

  public ResultActions sign(String pack, String contact) throws Exception {
    return postJson(
      "/api/conference/sponsorships",
      "{\"company\":\"Acme\",\"contactName\":\"Wile Coyote\",\"contactEmail\":\"%s\",\"pack\":\"%s\"}".formatted(contact, pack)
    );
  }

  public String paidSponsor(String pack, String contact) throws Exception {
    var sponsor = read(sign(pack, contact).andExpect(status().isCreated()), "$.id");
    postJson("/api/conference/sponsorships/" + sponsor + "/payment", "{}").andExpect(status().isOk());

    return sponsor;
  }

  public ResultActions place(String talk, String room, LocalDate day, int slot) throws Exception {
    return postJson("/api/conference/program/sessions", "{\"talkId\":\"%s\",\"room\":\"%s\",\"day\":\"%s\",\"slot\":%d}".formatted(talk, room, day, slot));
  }

  public ResultActions get(String url) throws Exception {
    return rest.perform(MockMvcRequestBuilders.get(url));
  }

  public ResultActions postJson(String url, String body) throws Exception {
    return rest.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(body));
  }

  public static String proposal(String format, String track, Speaker... speakers) {
    var speakersJson = Stream.of(speakers).map(Speaker::json).collect(Collectors.joining(","));

    return """
    {"title":"Hexagonal for real","summary":"Ports and adapters","format":"%s","level":"INTERMEDIATE","track":"%s","speakers":[%s]}
    """.formatted(format, track, speakersJson);
  }

  public static String attendee(String name, boolean student) {
    return "{\"name\":\"%s\",\"email\":\"%s\",\"student\":%s}".formatted(name, emailOf(name), student);
  }

  public static String emailOf(String name) {
    return name.toLowerCase() + "@mail.com";
  }

  public static String unique(String name) {
    return name + "-" + UUID.randomUUID().toString().substring(0, 8);
  }

  public static Block freeBlock() {
    return BLOCKS.get(USED_BLOCKS.getAndIncrement());
  }

  public static String read(ResultActions result, String path) throws Exception {
    return JsonPath.read(result.andReturn().getResponse().getContentAsString(), path).toString();
  }

  public record Speaker(String name, String company, boolean fromAbroad, boolean spokeBefore) {
    public static Speaker newcomer() {
      return new Speaker(unique("speaker"), unique("company"), false, false);
    }

    public static Speaker foreigner() {
      return new Speaker(unique("speaker"), unique("company"), true, false);
    }

    public String email() {
      return emailOf(name);
    }

    String json() {
      return "{\"email\":\"%s\",\"name\":\"%s\",\"company\":\"%s\",\"fromAbroad\":%s,\"spokeBefore\":%s}".formatted(
        email(),
        name,
        company,
        fromAbroad,
        spokeBefore
      );
    }
  }

  public record Block(LocalDate day, int firstSlot) {}
}
