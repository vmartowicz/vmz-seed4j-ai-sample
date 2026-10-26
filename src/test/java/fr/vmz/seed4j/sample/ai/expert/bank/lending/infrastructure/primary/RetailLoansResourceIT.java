package fr.vmz.seed4j.sample.ai.expert.bank.lending.infrastructure.primary;

import static fr.vmz.seed4j.sample.ai.expert.bank.BankApi.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.vmz.seed4j.sample.ai.IntegrationTest;
import fr.vmz.seed4j.sample.ai.expert.bank.BankApi;
import fr.vmz.seed4j.sample.ai.expert.bank.BankClock;
import fr.vmz.seed4j.sample.ai.expert.bank.BankClockConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import fr.vmz.seed4j.sample.ai.expert.bank.lending.application.LendingApplicationService;
import java.util.UUID;
import org.springframework.test.web.servlet.ResultActions;

@IntegrationTest
@AutoConfigureMockMvc
@Import(BankClockConfiguration.class)
class RetailLoansResourceIT {

  @Autowired
  private MockMvc rest;

  @Autowired
  private BankClock clock;

  @Autowired
  private LendingApplicationService lending;

  private BankApi api;

  @BeforeEach
  void loadApi() {
    api = new BankApi(rest);
    clock.at(NOW.minusYears(1));
  }

  @Test
  void shouldLendToLongStandingCustomerAndCollectInstalments() throws Exception {
    var customer = api.customer("3000");
    var account = api.account("CURRENT", customer);
    clock.at(NOW);

    var loan = read(
      apply(customer, "10000", 24)
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.borrower").value(customer))
        .andExpect(jsonPath("$.account").value(account))
        .andExpect(jsonPath("$.instalment").value(441.21))
        .andExpect(jsonPath("$.status").value("SIGNED")),
      "$.id"
    );
    clock.at(NOW.plusDays(14));
    lending.disburse();
    api.get("/api/retail-bank/loans/" + loan).andExpect(jsonPath("$.status").value("ACTIVE")).andExpect(jsonPath("$.nextInstalmentOn").value("2026-07-15"));
    api.balanceOf(account).andExpect(jsonPath("$.balance").value(10000));

    api.postJson("/api/retail-bank/compliance/sanctions", "{\"name\":\"Blocked " + loan + "\"}");
    api.transfer(account, api.payee(account, "Blocked " + loan, "FR7630006000011234567890189"), "1", "INSTANT");
    clock.at(NOW.plusDays(14).plusMonths(1));
    lending.collectInstalments();

    api.get("/api/retail-bank/loans/" + loan).andExpect(jsonPath("$.paidInstalments").value(1));
    api.balanceOf(account).andExpect(jsonPath("$.balance").value(9558.79)).andExpect(jsonPath("$.frozen").value(true));
  }

  @Test
  void shouldSendLoanToCollectionAfterTwoUnpaidInstalments() throws Exception {
    var customer = api.customer("3000");
    var account = api.account("CURRENT", customer);
    clock.at(NOW);
    var loan = read(apply(customer, "10000", 24), "$.id");
    clock.at(NOW.plusDays(14));
    lending.disburse();
    var garage = api.payee(account, "Garage", "FR7630006000011234567890189");
    api.transfer(account, garage, "5800", "INSTANT").andExpect(jsonPath("$.status").value("EXECUTED"));
    api.transfer(account, garage, "5800", "INSTANT").andExpect(jsonPath("$.status").value("EXECUTED"));
    clock.at(NOW.plusDays(14).plusMonths(1));

    lending.collectInstalments();
    clock.at(NOW.plusDays(14).plusMonths(2));
    lending.collectInstalments();

    api.get("/api/retail-bank/loans/" + loan).andExpect(jsonPath("$.status").value("IN_COLLECTION")).andExpect(jsonPath("$.unpaidInstalments").value(2));
  }

  @Test
  void shouldNotLendToRecentCustomers() throws Exception {
    clock.at(NOW);
    var customer = api.customer("3000");
    api.account("CURRENT", customer);

    apply(customer, "10000", 24).andExpect(status().isConflict()).andExpect(jsonPath("$.detail").value(containsString("6 months")));
  }

  @Test
  void shouldNotLendBeyond35PercentDebtRatio() throws Exception {
    var customer = api.customer("1000");
    api.account("CURRENT", customer);
    clock.at(NOW);

    apply(customer, "10000", 24).andExpect(status().isConflict()).andExpect(jsonPath("$.detail").value(containsString("35%")));
  }

  @Test
  void shouldNotLendAfterUnauthorizedOverdraft() throws Exception {
    var customer = api.customer("100");
    var account = api.account("CURRENT", customer);
    clock.at(NOW.minusMonths(2));
    api.pay(api.card(account, "CLASSIC"), "150", "FR");
    clock.at(NOW);

    apply(customer, "1000", 84).andExpect(status().isConflict()).andExpect(jsonPath("$.detail").value(containsString("overdraft")));
  }

  @Test
  void shouldNotLendOutsideLimits() throws Exception {
    apply(api.customer("3000"), "80000", 24).andExpect(status().isBadRequest());
    apply(UUID.randomUUID().toString(), "10000", 24).andExpect(status().isNotFound());
    api.get("/api/retail-bank/loans/" + UUID.randomUUID()).andExpect(status().isNotFound()).andExpect(jsonPath("$.title").value("Unknown element"));
  }

  private ResultActions apply(String customer, String amount, int months) throws Exception {
    return api.postJson("/api/retail-bank/loans", "{\"borrower\":\"%s\",\"amount\":%s,\"months\":%d}".formatted(customer, amount, months));
  }
}
