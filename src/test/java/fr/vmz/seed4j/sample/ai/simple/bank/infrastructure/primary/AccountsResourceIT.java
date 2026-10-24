package fr.vmz.seed4j.sample.ai.simple.bank.infrastructure.primary;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.jayway.jsonpath.JsonPath;
import fr.vmz.seed4j.sample.ai.IntegrationTest;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@IntegrationTest
@AutoConfigureMockMvc
class AccountsResourceIT {

  @Autowired
  private MockMvc rest;

  @Test
  void shouldOpenAccount() throws Exception {
    postJson("/api/bank/accounts", "{\"holderName\":\"Jane Doe\"}")
      .andExpect(status().isCreated())
      .andExpect(jsonPath("$.id").isNotEmpty())
      .andExpect(jsonPath("$.holderName").value("Jane Doe"))
      .andExpect(jsonPath("$.balance").value(0));
  }

  @Test
  void shouldNotOpenAccountWithoutHolder() throws Exception {
    postJson("/api/bank/accounts", "{}").andExpect(status().isBadRequest()).andExpect(jsonPath("$.title").value("Invalid value"));
  }

  @Test
  void shouldViewBalance() throws Exception {
    var accountId = openAccount();
    move(accountId, "transfers", "100.50", "Salary");

    rest
      .perform(get("/api/bank/accounts/" + accountId))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.balance").value(100.5));
  }

  @Test
  void shouldNotViewUnknownAccount() throws Exception {
    rest
      .perform(get("/api/bank/accounts/" + UUID.randomUUID()))
      .andExpect(status().isNotFound())
      .andExpect(jsonPath("$.title").value("Unknown element"));
  }

  @Test
  void shouldReceiveTransfer() throws Exception {
    var accountId = openAccount();

    move(accountId, "transfers", "100", "Salary")
      .andExpect(status().isCreated())
      .andExpect(jsonPath("$.type").value("TRANSFER_RECEIVED"))
      .andExpect(jsonPath("$.amount").value(100))
      .andExpect(jsonPath("$.label").value("Salary"))
      .andExpect(jsonPath("$.date").isNotEmpty())
      .andExpect(jsonPath("$.balanceAfter").value(100));
  }

  @Test
  void shouldMakePaymentWithoutLabel() throws Exception {
    var accountId = openAccount();
    move(accountId, "transfers", "100", "Salary");

    postJson("/api/bank/accounts/" + accountId + "/payments", "{\"amount\":30}")
      .andExpect(status().isCreated())
      .andExpect(jsonPath("$.type").value("PAYMENT"))
      .andExpect(jsonPath("$.label").doesNotExist())
      .andExpect(jsonPath("$.balanceAfter").value(70));
  }

  @Test
  void shouldNotPayMoreThanBalance() throws Exception {
    var accountId = openAccount();
    move(accountId, "transfers", "100", "Salary");

    move(accountId, "payments", "100.01", "Bakery")
      .andExpect(status().isConflict())
      .andExpect(jsonPath("$.title").value("Business rule violation"));
  }

  @Test
  void shouldNotMoveAmountWithMoreThan2Decimals() throws Exception {
    move(openAccount(), "transfers", "10.005", "Salary")
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.detail").value(containsString("10.005")));
  }

  @Test
  void shouldNotMoveNegativeAmount() throws Exception {
    move(openAccount(), "transfers", "-10", "Salary").andExpect(status().isBadRequest());
  }

  @Test
  void shouldViewHistoryFromMostRecent() throws Exception {
    var accountId = openAccount();
    move(accountId, "transfers", "100", "Salary");
    move(accountId, "payments", "20", "Bakery");

    rest
      .perform(get("/api/bank/accounts/" + accountId + "/operations"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$[*].label", contains("Bakery", "Salary")))
      .andExpect(jsonPath("$[*].balanceAfter", contains(80.0, 100.0)));
  }

  private String openAccount() throws Exception {
    var result = postJson("/api/bank/accounts", "{\"holderName\":\"Jane Doe\"}").andExpect(status().isCreated()).andReturn();

    return JsonPath.read(result.getResponse().getContentAsString(), "$.id");
  }

  private ResultActions move(String accountId, String operation, String amount, String label) throws Exception {
    return postJson("/api/bank/accounts/" + accountId + "/" + operation, "{\"amount\":" + amount + ",\"label\":\"" + label + "\"}");
  }

  private ResultActions postJson(String url, String body) throws Exception {
    return rest.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(body));
  }
}
