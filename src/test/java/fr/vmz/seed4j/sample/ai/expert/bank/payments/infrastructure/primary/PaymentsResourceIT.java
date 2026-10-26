package fr.vmz.seed4j.sample.ai.expert.bank.payments.infrastructure.primary;

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
import fr.vmz.seed4j.sample.ai.expert.bank.payments.application.PaymentsApplicationService;
import java.util.UUID;

@IntegrationTest
@AutoConfigureMockMvc
@Import(BankClockConfiguration.class)
class PaymentsResourceIT {

  private static final String OTHER_BANK = "FR7630006000011234567890189";

  @Autowired
  private MockMvc rest;

  @Autowired
  private BankClock clock;

  @Autowired
  private PaymentsApplicationService payments;

  private BankApi api;

  @BeforeEach
  void loadApi() {
    api = new BankApi(rest);
    clock.at(NOW);
  }

  @Test
  void shouldExecuteInstantTransferBetweenOurCustomers() throws Exception {
    var payer = api.fundedAccount("500");
    var beneficiary = api.fundedAccount("10");
    var payee = api.payee(payer, "John Doe", beneficiary);

    api
      .transfer(payer, payee, "200", "INSTANT")
      .andExpect(status().isCreated())
      .andExpect(jsonPath("$.id").isNotEmpty())
      .andExpect(jsonPath("$.payer").value(payer))
      .andExpect(jsonPath("$.payeeName").value("John Doe"))
      .andExpect(jsonPath("$.payeeIban").value(beneficiary))
      .andExpect(jsonPath("$.amount").value(200))
      .andExpect(jsonPath("$.kind").value("INSTANT"))
      .andExpect(jsonPath("$.status").value("EXECUTED"));

    api.balanceOf(payer).andExpect(jsonPath("$.balance").value(300));
    api.balanceOf(beneficiary).andExpect(jsonPath("$.balance").value(210));
  }

  @Test
  void shouldExecuteStandardTransferOnNextBusinessDay() throws Exception {
    var payer = api.fundedAccount("500");
    var payee = api.payee(payer, "Landlord", OTHER_BANK);
    var transfer = read(api.transfer(payer, payee, "450", "STANDARD").andExpect(jsonPath("$.status").value("SCHEDULED")).andExpect(jsonPath("$.executionDate").value("2026-06-02")), "$.id");
    clock.at(NOW.plusDays(1).withHour(7));

    payments.executeDueTransfers();

    api.get("/api/retail-bank/transfers/" + transfer).andExpect(jsonPath("$.status").value("EXECUTED"));
    api.balanceOf(payer).andExpect(jsonPath("$.balance").value(50));
  }

  @Test
  void shouldRejectTransferNotCoveredByBalanceAndOverdraft() throws Exception {
    var payer = api.fundedAccount("100");
    var payee = api.payee(payer, "Landlord", OTHER_BANK);

    api.transfer(payer, payee, "1600.01", "INSTANT").andExpect(jsonPath("$.status").value("REJECTED")).andExpect(jsonPath("$.rejection").value("Insufficient funds"));
    api.transfer(payer, payee, "1600", "INSTANT").andExpect(jsonPath("$.status").value("EXECUTED"));
  }

  @Test
  void shouldBlockTransferToSanctionedPayeeAndFreezeAccount() throws Exception {
    var payer = api.fundedAccount("500");
    var sanctioned = "Evil Corp " + UUID.randomUUID();
    api.postJson("/api/retail-bank/compliance/sanctions", "{\"name\":\"" + sanctioned + "\"}").andExpect(status().isCreated());
    var payee = api.payee(payer, sanctioned.toUpperCase(), OTHER_BANK);

    api.transfer(payer, payee, "10", "INSTANT").andExpect(jsonPath("$.status").value("BLOCKED"));

    api.balanceOf(payer).andExpect(jsonPath("$.frozen").value(true)).andExpect(jsonPath("$.freezes").value(contains("SANCTIONS")));
    api.get("/api/retail-bank/compliance/alerts").andExpect(jsonPath("$[?(@.account == '" + payer + "')].reason").value(contains(containsString(sanctioned))));
    var otherPayee = api.payee(payer, "Landlord", OTHER_BANK);
    api.transfer(payer, otherPayee, "10", "INSTANT").andExpect(jsonPath("$.rejection").value("The account is frozen"));
  }

  @Test
  void shouldHoldBigTransferToNewPayeeUntilAdvisorCalls() throws Exception {
    var payer = api.fundedAccount("12000");
    var payee = api.payee(payer, "Car dealer", OTHER_BANK);

    var transfer = read(api.transfer(payer, payee, "11000", "INSTANT").andExpect(jsonPath("$.status").value("HELD")), "$.id");
    api.balanceOf(payer).andExpect(jsonPath("$.balance").value(12000));

    api.postJson("/api/retail-bank/transfers/" + transfer + "/release", "{}").andExpect(status().isOk()).andExpect(jsonPath("$.status").value("EXECUTED"));
    api.postJson("/api/retail-bank/transfers/" + transfer + "/release", "{}").andExpect(status().isConflict());
    api.balanceOf(payer).andExpect(jsonPath("$.balance").value(1000));
  }

  @Test
  void shouldNotSendInstantTransferAbove15000() throws Exception {
    var payer = api.fundedAccount("20000");

    api.transfer(payer, api.payee(payer, "Notary", OTHER_BANK), "15000.01", "INSTANT").andExpect(status().isConflict());
  }

  @Test
  void shouldCollectDirectDebitAndRefundItWithin8Weeks() throws Exception {
    var account = api.fundedAccount("100");
    var mandate = read(api.postJson("/api/retail-bank/accounts/" + account + "/mandates", "{\"creditor\":\"EDF\"}").andExpect(status().isCreated()).andExpect(jsonPath("$.creditor").value("EDF")), "$.id");

    var debit = read(
      api.postJson("/api/retail-bank/mandates/" + mandate + "/collections", "{\"amount\":80}").andExpect(status().isCreated()).andExpect(jsonPath("$.status").value("COLLECTED")),
      "$.id"
    );
    api.balanceOf(account).andExpect(jsonPath("$.balance").value(20));
    clock.at(NOW.plusWeeks(7));

    api.postJson("/api/retail-bank/direct-debits/" + debit + "/refund", "{}").andExpect(status().isOk()).andExpect(jsonPath("$.status").value("REFUNDED"));
    api.balanceOf(account).andExpect(jsonPath("$.balance").value(100));
  }

  @Test
  void shouldCharge20EurosForDirectDebitRejectedOnFrozenAccount() throws Exception {
    var account = api.fundedAccount("100");
    var mandate = read(api.postJson("/api/retail-bank/accounts/" + account + "/mandates", "{\"creditor\":\"EDF\"}"), "$.id");
    var sanctioned = "Bad Guys " + UUID.randomUUID();
    api.postJson("/api/retail-bank/compliance/sanctions", "{\"name\":\"" + sanctioned + "\"}");
    api.transfer(account, api.payee(account, sanctioned, OTHER_BANK), "1", "INSTANT");

    api.postJson("/api/retail-bank/mandates/" + mandate + "/collections", "{\"amount\":80}").andExpect(jsonPath("$.status").value("REJECTED"));

    api.balanceOf(account).andExpect(jsonPath("$.balance").value(80));
  }

  @Test
  void shouldNotCollectRevokedMandate() throws Exception {
    var account = api.fundedAccount("100");
    var mandate = read(api.postJson("/api/retail-bank/accounts/" + account + "/mandates", "{\"creditor\":\"EDF\"}"), "$.id");

    api.postJson("/api/retail-bank/mandates/" + mandate + "/revocation", "{}").andExpect(status().isOk()).andExpect(jsonPath("$.active").value(false));

    api.postJson("/api/retail-bank/mandates/" + mandate + "/collections", "{\"amount\":80}").andExpect(status().isConflict());
  }

  @Test
  void shouldNotUseUnknownPayeeOrAccount() throws Exception {
    var payer = api.fundedAccount("100");

    api.transfer(payer, UUID.randomUUID().toString(), "10", "INSTANT").andExpect(status().isNotFound()).andExpect(jsonPath("$.title").value("Unknown element"));
    api.postJson("/api/retail-bank/accounts/" + OTHER_BANK + "/payees", "{\"name\":\"Jane\",\"iban\":\"" + OTHER_BANK + "\"}").andExpect(status().isNotFound());
    api.get("/api/retail-bank/transfers/" + UUID.randomUUID()).andExpect(status().isNotFound());
  }

  @Test
  void shouldNotTransferWithoutReason() throws Exception {
    var payer = api.fundedAccount("100");
    var payee = api.payee(payer, "Landlord", OTHER_BANK);

    api
      .postJson("/api/retail-bank/transfers", "{\"payer\":\"%s\",\"payeeId\":\"%s\",\"amount\":10,\"kind\":\"INSTANT\"}".formatted(payer, payee))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.title").value("Invalid value"));
  }
}
