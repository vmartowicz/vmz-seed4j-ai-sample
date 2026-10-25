package fr.vmz.seed4j.sample.ai.expert.gym.billing.infrastructure.secondary.bank;

import static fr.vmz.seed4j.sample.ai.expert.gym.billing.domain.BillingFixture.*;

import ch.qos.logback.classic.Level;
import fr.vmz.seed4j.sample.ai.Logs;
import fr.vmz.seed4j.sample.ai.LogsSpy;
import fr.vmz.seed4j.sample.ai.UnitTest;
import org.junit.jupiter.api.Test;

@UnitTest
class LoggingBankTest {

  @Logs
  private LogsSpy logs;

  @Test
  void shouldLogDirectDebit() {
    var invoice = monthlyInvoice();

    new LoggingBank().submitDebit(invoice);

    logs.shouldHave(Level.INFO, "Direct debit 1 of 29.90 submitted for invoice " + invoice.id().get());
  }
}
