package fr.vmz.seed4j.sample.ai.expert.gym.billing.infrastructure.primary;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.vmz.seed4j.sample.ai.IntegrationTest;
import fr.vmz.seed4j.sample.ai.expert.gym.GymApi;
import fr.vmz.seed4j.sample.ai.expert.gym.GymClock;
import fr.vmz.seed4j.sample.ai.expert.gym.GymClockConfiguration;
import java.time.LocalDate;
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
@Import(GymClockConfiguration.class)
class BillingResourceIT {

  @Autowired
  private MockMvc rest;

  @Autowired
  private GymClock clock;

  @Autowired
  private DailyBillingJob dailyBilling;

  private GymApi api;

  @BeforeEach
  void loadApi() {
    api = new GymApi(rest);
    on(LocalDate.of(2026, 3, 10));
  }

  @Test
  void shouldBillFirstMonthProRataWithSignUpFee() throws Exception {
    var member = api.member("ESSENTIAL", "ECULLY");

    statement(member.id())
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.memberId").value(member.id()))
      .andExpect(jsonPath("$.status").value("AWAITING_FIRST_PAYMENT"))
      .andExpect(jsonPath("$.monthlyFee").value(29.9))
      .andExpect(jsonPath("$.invoices[0].id").value(member.firstInvoice()))
      .andExpect(jsonPath("$.invoices[0].kind").value("FIRST_MONTH"))
      .andExpect(jsonPath("$.invoices[0].period").value("2026-03"))
      .andExpect(jsonPath("$.invoices[0].amount").value(60.22))
      .andExpect(jsonPath("$.invoices[0].status").value("AWAITING_PAYMENT"));
  }

  @Test
  void shouldNotPayFirstInvoiceTwice() throws Exception {
    var member = api.activeMember("ESSENTIAL", "ECULLY");

    api.payByCard(member.firstInvoice()).andExpect(status().isConflict());
  }

  @Test
  void shouldDebitMonthlyFeeOnThe5th() throws Exception {
    var member = api.activeMember("PREMIUM", "PART_DIEU");
    on(LocalDate.of(2026, 4, 5));

    dailyBilling.run();

    var monthly = GymApi.read(statement(member.id()).andExpect(jsonPath("$.invoices[1].kind").value("MONTHLY")), "$.invoices[1].id");
    debitResult(monthly, true).andExpect(status().isOk()).andExpect(jsonPath("$.status").value("PAID"));
    statement(member.id())
      .andExpect(jsonPath("$.status").value("UP_TO_DATE"))
      .andExpect(jsonPath("$.invoices[1].period").value("2026-04"))
      .andExpect(jsonPath("$.invoices[1].amount").value(49.9));
  }

  @Test
  void shouldBlockAccessAfterThreeRejectedDebitsUntilSettlement() throws Exception {
    var member = api.activeMember("ESSENTIAL", "ECULLY");
    var monthly = rejectedThreeTimes(member.id());

    statement(member.id())
      .andExpect(jsonPath("$.status").value("IN_ARREARS"))
      .andExpect(jsonPath("$.arrearsSince").value("2026-04-15"))
      .andExpect(jsonPath("$.invoices[1].id").value(monthly))
      .andExpect(jsonPath("$.invoices[1].status").value("UNPAID"))
      .andExpect(jsonPath("$.invoices[1].attempts").value(3))
      .andExpect(jsonPath("$.invoices[2].kind").value("ARREARS_FEE"))
      .andExpect(jsonPath("$.invoices[2].amount").value(10));
    api.enter("ECULLY", member.badge()).andExpect(jsonPath("$.accepted").value(false)).andExpect(jsonPath("$.refusal").value("IN_ARREARS"));

    api
      .postJson("/api/gym/billing/accounts/" + member.id() + "/settlement", "{}")
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.status").value("UP_TO_DATE"))
      .andExpect(jsonPath("$.invoices[1].status").value("PAID"))
      .andExpect(jsonPath("$.invoices[2].status").value("PAID"));
    api.enter("ECULLY", member.badge()).andExpect(jsonPath("$.accepted").value(true));
  }

  @Test
  void shouldTerminateMembershipAfter2MonthsOfArrears() throws Exception {
    var member = api.activeMember("ESSENTIAL", "VILLEURBANNE");
    rejectedThreeTimes(member.id());
    on(LocalDate.of(2026, 6, 15));

    dailyBilling.run();

    statement(member.id()).andExpect(jsonPath("$.status").value("CLOSED"));
    api.get("/api/gym/memberships/" + member.id()).andExpect(jsonPath("$.status").value("TERMINATED"));
    api.enter("VILLEURBANNE", member.badge()).andExpect(jsonPath("$.refusal").value("MEMBERSHIP_INACTIVE"));
  }

  @Test
  void shouldNotDebitSuspendedMember() throws Exception {
    var member = api.activeMember("ESSENTIAL", "ECULLY");
    api.postJson("/api/gym/memberships/" + member.id() + "/suspension", "{\"start\":\"2026-04-01\",\"months\":1}").andExpect(status().isOk());
    on(LocalDate.of(2026, 4, 5));

    dailyBilling.run();

    statement(member.id()).andExpect(jsonPath("$.invoices.length()").value(1));
  }

  @Test
  void shouldNotSettleAccountWithoutArrears() throws Exception {
    var member = api.activeMember("ESSENTIAL", "ECULLY");

    api.postJson("/api/gym/billing/accounts/" + member.id() + "/settlement", "{}").andExpect(status().isConflict());
  }

  @Test
  void shouldNotAcceptDebitResultWithoutDebitInProgress() throws Exception {
    var member = api.member("ESSENTIAL", "ECULLY");

    debitResult(member.firstInvoice(), true).andExpect(status().isConflict());
  }

  @Test
  void shouldNotFindUnknownAccountOrInvoice() throws Exception {
    statement(UUID.randomUUID().toString()).andExpect(status().isNotFound());
    api.payByCard(UUID.randomUUID().toString()).andExpect(status().isNotFound()).andExpect(jsonPath("$.title").value("Unknown element"));
  }

  private String rejectedThreeTimes(String member) throws Exception {
    on(LocalDate.of(2026, 4, 5));
    dailyBilling.run();
    var monthly = GymApi.read(statement(member), "$.invoices[1].id");
    debitResult(monthly, false).andExpect(jsonPath("$.status").value("DEBIT_RETRY_SCHEDULED")).andExpect(jsonPath("$.nextAttemptOn").value("2026-04-08"));

    on(LocalDate.of(2026, 4, 8));
    dailyBilling.run();
    debitResult(monthly, false).andExpect(jsonPath("$.nextAttemptOn").value("2026-04-15"));

    on(LocalDate.of(2026, 4, 15));
    dailyBilling.run();
    debitResult(monthly, false).andExpect(status().isOk()).andExpect(jsonPath("$.status").value("UNPAID"));

    return monthly;
  }

  private ResultActions statement(String member) throws Exception {
    return api.get("/api/gym/billing/accounts/" + member);
  }

  private ResultActions debitResult(String invoice, boolean accepted) throws Exception {
    return api.postJson("/api/gym/billing/invoices/" + invoice + "/debit-result", "{\"accepted\":" + accepted + "}");
  }

  private void on(LocalDate day) {
    clock.at(day.atTime(6, 0));
  }
}
