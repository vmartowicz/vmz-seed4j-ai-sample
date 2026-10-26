package fr.vmz.seed4j.sample.ai.expert.bank.accounts.infrastructure.primary;

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
import fr.vmz.seed4j.sample.ai.expert.bank.accounts.application.RetailAccountsApplicationService;
import java.util.UUID;

@IntegrationTest
@AutoConfigureMockMvc
@Import(BankClockConfiguration.class)
class BankAccountsResourceIT {

  @Autowired
  private MockMvc rest;

  @Autowired
  private BankClock clock;

  @Autowired
  private RetailAccountsApplicationService accounts;

  private BankApi api;

  @BeforeEach
  void loadApi() {
    api = new BankApi(rest);
    clock.at(NOW);
  }

  @Test
  void shouldOpenCurrentAccountWithOverdraftOfOneMonthOfIncome() throws Exception {
    var customer = api.customer("1800");

    var iban = read(
      api
        .open("CURRENT", customer)
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.iban").value(startsWith("FR")))
        .andExpect(jsonPath("$.type").value("CURRENT"))
        .andExpect(jsonPath("$.holders").value(contains(customer)))
        .andExpect(jsonPath("$.authorizedOverdraft").value(1800))
        .andExpect(jsonPath("$.balance").value(0))
        .andExpect(jsonPath("$.frozen").value(false)),
      "$.iban"
    );

    api.deposit(iban, "250").andExpect(status().isOk()).andExpect(jsonPath("$.balance").value(250));
    api.get("/api/retail-bank/accounts/" + iban.substring(0, 4) + " " + iban.substring(4)).andExpect(jsonPath("$.entries[0].label").value("Cash deposit"));
  }

  @Test
  void shouldCapLivretA() throws Exception {
    var savings = api.account("SAVINGS", api.customer("1500"));
    api.deposit(savings, "22950").andExpect(status().isOk());

    api.deposit(savings, "1").andExpect(status().isConflict()).andExpect(jsonPath("$.detail").value(containsString("22950")));
  }

  @Test
  void shouldOpenYouthAccountsForMinorsOnly() throws Exception {
    var minor = read(api.onboard("2012-01-01", "0", "2026-05-15", false, "John Doe"), "$.id");

    var youth = api.account("YOUTH", minor);
    api.open("CURRENT", minor).andExpect(status().isConflict());
    api.open("YOUTH", api.customer("1000")).andExpect(status().isConflict());
    api.postJson("/api/retail-bank/accounts/" + youth + "/cards", "{\"type\":\"GOLD\"}").andExpect(status().isConflict());
  }

  @Test
  void shouldNotOpenAccountForUnknownCustomer() throws Exception {
    api.open("CURRENT", UUID.randomUUID().toString()).andExpect(status().isNotFound()).andExpect(jsonPath("$.title").value("Unknown element"));
  }

  @Test
  void shouldPayByCardWithinLimitsAndOverdraft() throws Exception {
    var iban = api.fundedAccount("100");
    var card = api.card(iban, "CLASSIC");

    api.pay(card, "1400", "FR").andExpect(status().isOk()).andExpect(jsonPath("$.accepted").value(true));
    api.pay(card, "101", "FR").andExpect(jsonPath("$.accepted").value(false)).andExpect(jsonPath("$.refusal").value("LIMIT_REACHED"));

    api.get("/api/retail-bank/accounts/" + iban).andExpect(jsonPath("$.balance").value(-1300));
  }

  @Test
  void shouldChargeFeesBeyondAuthorizedOverdraft() throws Exception {
    var iban = api.account("CURRENT", api.customer("100"));
    var card = api.card(iban, "GOLD");

    api.pay(card, "150", "FR").andExpect(jsonPath("$.accepted").value(true));

    api.get("/api/retail-bank/accounts/" + iban).andExpect(jsonPath("$.balance").value(-158)).andExpect(jsonPath("$.unauthorizedOverdraft").value(true));
  }

  @Test
  void shouldBlockCardUsedInTwoCountriesWithinAnHour() throws Exception {
    var card = api.card(api.fundedAccount("500"), "CLASSIC");
    api.pay(card, "20", "FR");
    clock.at(NOW.plusMinutes(30));

    api.pay(card, "20", "ES").andExpect(jsonPath("$.refusal").value("FRAUD_SUSPECTED"));

    api.get("/api/retail-bank/cards/" + card).andExpect(jsonPath("$.status").value("BLOCKED"));
  }

  @Test
  void shouldBlockCardAfterThreeWrongPinsAndOpposeLostCard() throws Exception {
    var iban = api.fundedAccount("500");
    var blocked = api.card(iban, "CLASSIC");
    var lost = api.card(iban, "GOLD");

    for (int attempt = 0; attempt < 3; attempt++) {
      api.postJson("/api/retail-bank/cards/" + blocked + "/wrong-pin", "{}").andExpect(status().isOk());
    }
    api.postJson("/api/retail-bank/cards/" + lost + "/opposition", "{}").andExpect(status().isOk()).andExpect(jsonPath("$.status").value("OPPOSED"));

    api.get("/api/retail-bank/cards/" + blocked).andExpect(jsonPath("$.status").value("BLOCKED")).andExpect(jsonPath("$.type").value("CLASSIC"));
    api.postJson("/api/retail-bank/cards/" + lost + "/opposition", "{}").andExpect(status().isConflict());
    api.postJson("/api/retail-bank/cards/" + lost + "/withdrawals", "{\"amount\":20}").andExpect(jsonPath("$.refusal").value("CARD_NOT_ACTIVE"));
  }

  @Test
  void shouldWithdrawCash() throws Exception {
    var iban = api.fundedAccount("500");
    var card = api.card(iban, "CLASSIC");

    api.postJson("/api/retail-bank/cards/" + card + "/withdrawals", "{\"amount\":200}").andExpect(jsonPath("$.accepted").value(true));

    api.get("/api/retail-bank/accounts/" + iban).andExpect(jsonPath("$.balance").value(300));
  }

  @Test
  void shouldChargeAgiosAndMonthlyFee() throws Exception {
    var iban = api.account("CURRENT", api.customer("2000"));
    api.pay(api.card(iban, "GOLD"), "1000", "FR");

    accounts.accrueInterest();
    accounts.closeMonth();

    api.get("/api/retail-bank/accounts/" + iban).andExpect(jsonPath("$.balance").value(-1002.44));
  }

  @Test
  void shouldNotIssueCardOnSavings() throws Exception {
    var savings = api.account("SAVINGS", api.customer("1500"));

    api.postJson("/api/retail-bank/accounts/" + savings + "/cards", "{\"type\":\"CLASSIC\"}").andExpect(status().isConflict());
  }

  @Test
  void shouldNotFindUnknownAccountOrCard() throws Exception {
    api.get("/api/retail-bank/accounts/FR7630006000011234567890189").andExpect(status().isNotFound());
    api.get("/api/retail-bank/accounts/FR7630006000011234567890188").andExpect(status().isBadRequest()).andExpect(jsonPath("$.title").value("Invalid value"));
    api.get("/api/retail-bank/cards/" + UUID.randomUUID()).andExpect(status().isNotFound());
  }

  @Test
  void shouldNotDepositWithoutAmount() throws Exception {
    api.postJson("/api/retail-bank/accounts/" + api.fundedAccount("1") + "/deposits", "{}").andExpect(status().isBadRequest());
  }
}
