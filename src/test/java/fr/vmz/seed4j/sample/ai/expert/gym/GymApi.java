package fr.vmz.seed4j.sample.ai.expert.gym;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.jayway.jsonpath.JsonPath;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

public final class GymApi {

  private static final LocalDate FIRST_MONDAY = LocalDate.of(2027, 1, 4);
  private static final AtomicInteger USED_WEEKS = new AtomicInteger();
  private static final Map<String, AtomicInteger> USED_STUDIO_DAYS = new ConcurrentHashMap<>();

  private final MockMvc rest;

  public GymApi(MockMvc rest) {
    this.rest = rest;
  }

  public ResultActions subscribe(String plan, String homeGym, boolean committed, String birthDate, boolean parentalConsent) throws Exception {
    return postJson(
      "/api/gym/memberships",
      """
      {"fullName":"Jane Doe","birthDate":"%s","parentalConsent":%s,"studentCard":false,"plan":"%s","committed":%s,"homeGym":"%s"}
      """.formatted(birthDate, parentalConsent, plan, committed, homeGym)
    );
  }

  public Member member(String plan, String homeGym) throws Exception {
    var body = subscribe(plan, homeGym, true, "1990-05-04", false).andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();

    return new Member(JsonPath.read(body, "$.membership.id"), JsonPath.read(body, "$.badge"), JsonPath.read(body, "$.firstInvoice.id"));
  }

  public Member activeMember(String plan, String homeGym) throws Exception {
    var member = member(plan, homeGym);
    payByCard(member.firstInvoice()).andExpect(status().isOk());

    return member;
  }

  public ResultActions payByCard(String invoiceId) throws Exception {
    return postJson("/api/gym/billing/invoices/" + invoiceId + "/card-payment", "{}");
  }

  public ResultActions enter(String gym, String badge) throws Exception {
    return postJson("/api/gym/access/gyms/" + gym + "/entries", "{\"badge\":\"" + badge + "\"}");
  }

  public String coach(String discipline) throws Exception {
    var coach = postJson("/api/gym/schedule/coaches", "{\"name\":\"Coach " + discipline + "\",\"certifications\":[\"" + discipline + "\"]}");

    return read(coach.andExpect(status().isCreated()), "$.id");
  }

  public ResultActions scheduleClass(String coach, StudioDay place, String start, int minutes, int capacity) throws Exception {
    return postJson(
      "/api/gym/schedule/classes",
      """
      {"discipline":"YOGA","coachId":"%s","gym":"%s","studio":"%s","day":"%s","start":"%s","durationMinutes":%d,"capacity":%d}
      """.formatted(coach, place.gym(), place.studio(), place.day(), start, minutes, capacity)
    );
  }

  public String yogaClass(StudioDay place, String start, int capacity) throws Exception {
    return read(scheduleClass(coach("YOGA"), place, start, 45, capacity).andExpect(status().isCreated()), "$.id");
  }

  public ResultActions publish(LocalDate monday) throws Exception {
    return postJson("/api/gym/schedule/weeks/" + monday + "/publication", "{}");
  }

  public String sessionOf(LocalDate monday, String gymClass) throws Exception {
    List<String> ids = JsonPath.read(
      get("/api/gym/schedule/weeks/" + monday + "/sessions").andReturn().getResponse().getContentAsString(),
      "$[?(@.classId == '" + gymClass + "')].id"
    );

    return ids.getFirst();
  }

  public ResultActions book(String member, String session) throws Exception {
    return postJson("/api/gym/bookings", "{\"memberId\":\"" + member + "\",\"sessionId\":\"" + session + "\"}");
  }

  public ResultActions get(String url) throws Exception {
    return rest.perform(MockMvcRequestBuilders.get(url));
  }

  public ResultActions postJson(String url, String body) throws Exception {
    return rest.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(body));
  }

  public static String read(ResultActions result, String path) throws Exception {
    return JsonPath.read(result.andReturn().getResponse().getContentAsString(), path).toString();
  }

  public static LocalDate freeMonday() {
    LocalDate monday;
    do {
      monday = FIRST_MONDAY.plusWeeks(USED_WEEKS.getAndIncrement());
    } while (monday.getMonth() != monday.plusDays(6).getMonth());

    return monday;
  }

  public static StudioDay freeStudioDay(String gym) {
    var studios = "PART_DIEU".equals(gym) ? List.of("A", "B", "C") : List.of("A", "B");
    var used = USED_STUDIO_DAYS.computeIfAbsent(gym, key -> new AtomicInteger()).getAndIncrement();
    if (used >= studios.size() * 7) {
      throw new IllegalStateException("No free studio day left in " + gym);
    }

    return new StudioDay(gym, studios.get(used % studios.size()), DayOfWeek.of(used / studios.size() + 1));
  }

  public record Member(String id, String badge, String firstInvoice) {}

  public record StudioDay(String gym, String studio, DayOfWeek day) {
    public LocalDateTime at(LocalDate monday, String time) {
      return monday.plusDays(day.ordinal()).atTime(LocalTime.parse(time));
    }
  }
}
