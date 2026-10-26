package fr.vmz.seed4j.sample.ai.expert.bank.compliance.infrastructure.primary;

import static fr.vmz.seed4j.sample.ai.expert.bank.BankApi.*;
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

@IntegrationTest
@AutoConfigureMockMvc
@Import(BankClockConfiguration.class)
class ComplianceResourceIT {

  @Autowired
  private MockMvc rest;

  @Autowired
  private BankClock clock;

  private BankApi api;

  @BeforeEach
  void loadApi() {
    api = new BankApi(rest);
    clock.at(NOW);
  }

  @Test
  void shouldAddSanctionedName() throws Exception {
    api.postJson("/api/retail-bank/compliance/sanctions", "{\"name\":\" Evil Corp \"}").andExpect(status().isCreated()).andExpect(jsonPath("$.name").value("Evil Corp"));
    api.get("/api/retail-bank/compliance/alerts").andExpect(status().isOk());
  }

  @Test
  void shouldNotAddBlankSanctionedName() throws Exception {
    api.postJson("/api/retail-bank/compliance/sanctions", "{}").andExpect(status().isBadRequest()).andExpect(jsonPath("$.title").value("Invalid value"));
  }
}
