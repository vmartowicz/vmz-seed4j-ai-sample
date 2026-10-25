package fr.vmz.seed4j.sample.ai.advanced.leave.infrastructure.primary;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.jayway.jsonpath.JsonPath;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

final class LeaveApi {

  static final String MARC = "00000000-0000-4000-8000-000000000001";
  static final String EMMA = "00000000-0000-4000-8000-000000000002";
  static final String LUCAS = "00000000-0000-4000-8000-000000000003";
  static final String LEA = "00000000-0000-4000-8000-000000000004";
  static final String HUGO = "00000000-0000-4000-8000-000000000005";
  static final String CHLOE = "00000000-0000-4000-8000-000000000006";
  static final String SOPHIE = "00000000-0000-4000-8000-000000000007";
  static final String PAUL = "00000000-0000-4000-8000-000000000008";
  static final String INES = "00000000-0000-4000-8000-000000000009";

  private static final String EMPLOYEE_HEADER = "X-Employee-Id";

  private final MockMvc rest;

  LeaveApi(MockMvc rest) {
    this.rest = rest;
  }

  ResultActions request(String employee, String start, String end) throws Exception {
    return postAs(employee, "/api/leave/requests", period(start, "MORNING", end, "AFTERNOON"));
  }

  String requestId(String employee, String start, String end) throws Exception {
    var result = request(employee, start, end).andExpect(status().isCreated()).andReturn();

    return JsonPath.read(result.getResponse().getContentAsString(), "$.id");
  }

  ResultActions getAs(String employee, String url) throws Exception {
    return rest.perform(get(url).header(EMPLOYEE_HEADER, employee));
  }

  ResultActions postAs(String employee, String url, String body) throws Exception {
    return rest.perform(post(url).header(EMPLOYEE_HEADER, employee).contentType(MediaType.APPLICATION_JSON).content(body));
  }

  ResultActions putAs(String employee, String url, String body) throws Exception {
    return rest.perform(put(url).header(EMPLOYEE_HEADER, employee).contentType(MediaType.APPLICATION_JSON).content(body));
  }

  static String period(String start, String startHalf, String end, String endHalf) {
    return "{\"startDate\":\"%s\",\"startHalf\":\"%s\",\"endDate\":\"%s\",\"endHalf\":\"%s\"}".formatted(start, startHalf, end, endHalf);
  }
}
