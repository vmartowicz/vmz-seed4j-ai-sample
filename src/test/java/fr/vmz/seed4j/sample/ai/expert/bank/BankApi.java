package fr.vmz.seed4j.sample.ai.expert.bank;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.jayway.jsonpath.JsonPath;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

public final class BankApi {

  public static final LocalDateTime NOW = LocalDateTime.of(2026, 6, 1, 10, 0);

  private final MockMvc rest;

  public BankApi(MockMvc rest) {
    this.rest = rest;
  }

  public ResultActions onboard(String birthDate, String income, String proofOfAddress, boolean politicallyExposed, String guardian) throws Exception {
    var guardianJson = guardian == null ? "" : ",\"legalGuardian\":\"" + guardian + "\"";

    return postJson(
      "/api/retail-bank/customers",
      """
      {"fullName":"Jane Doe","birthDate":"%s","monthlyIncome":%s,"identityDocumentExpiry":"2030-01-01","proofOfAddressDate":"%s","incomeProofProvided":true,"politicallyExposed":%s,"risk":"LOW"%s}
      """.formatted(birthDate, income, proofOfAddress, politicallyExposed, guardianJson)
    );
  }

  public String customer(String income) throws Exception {
    return read(onboard("1990-05-04", income, "2026-05-15", false, null).andExpect(status().isCreated()), "$.id");
  }

  public ResultActions open(String type, String... holders) throws Exception {
    var ids = Stream.of(holders).map(holder -> "\"" + holder + "\"").collect(Collectors.joining(","));

    return postJson("/api/retail-bank/accounts", "{\"type\":\"" + type + "\",\"holders\":[" + ids + "]}");
  }

  public String account(String type, String... holders) throws Exception {
    return read(open(type, holders).andExpect(status().isCreated()), "$.iban");
  }

  public String fundedAccount(String balance) throws Exception {
    var iban = account("CURRENT", customer("1500"));
    deposit(iban, balance).andExpect(status().isOk());

    return iban;
  }

  public ResultActions deposit(String iban, String amount) throws Exception {
    return postJson("/api/retail-bank/accounts/" + iban + "/deposits", "{\"amount\":" + amount + "}");
  }

  public String card(String iban, String type) throws Exception {
    return read(postJson("/api/retail-bank/accounts/" + iban + "/cards", "{\"type\":\"" + type + "\"}").andExpect(status().isCreated()), "$.id");
  }

  public ResultActions pay(String card, String amount, String country) throws Exception {
    return postJson("/api/retail-bank/cards/" + card + "/payments", "{\"amount\":" + amount + ",\"country\":\"" + country + "\"}");
  }

  public String payee(String payer, String name, String iban) throws Exception {
    var payee = postJson("/api/retail-bank/accounts/" + payer + "/payees", "{\"name\":\"" + name + "\",\"iban\":\"" + iban + "\"}");

    return read(payee.andExpect(status().isCreated()), "$.id");
  }

  public ResultActions transfer(String payer, String payee, String amount, String kind) throws Exception {
    return postJson(
      "/api/retail-bank/transfers",
      "{\"payer\":\"%s\",\"payeeId\":\"%s\",\"amount\":%s,\"reason\":\"Rent\",\"kind\":\"%s\"}".formatted(payer, payee, amount, kind)
    );
  }

  public ResultActions balanceOf(String iban) throws Exception {
    return get("/api/retail-bank/accounts/" + iban);
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
}
