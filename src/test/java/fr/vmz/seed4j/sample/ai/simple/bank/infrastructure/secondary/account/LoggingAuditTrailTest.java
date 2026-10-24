package fr.vmz.seed4j.sample.ai.simple.bank.infrastructure.secondary.account;

import static fr.vmz.seed4j.sample.ai.simple.bank.domain.AccountsFixture.*;

import ch.qos.logback.classic.Level;
import fr.vmz.seed4j.sample.ai.Logs;
import fr.vmz.seed4j.sample.ai.LogsSpy;
import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.simple.bank.domain.AccountId;
import fr.vmz.seed4j.sample.ai.simple.bank.domain.AuditEvent;
import fr.vmz.seed4j.sample.ai.simple.bank.domain.AuditEventType;
import fr.vmz.seed4j.sample.ai.simple.bank.domain.Balance;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;

@UnitTest
class LoggingAuditTrailTest {

  private static final AccountId ACCOUNT = new AccountId(UUID.fromString("00000000-0000-4000-8000-000000000042"));

  @Logs
  private LogsSpy logs;

  private final LoggingAuditTrail audit = new LoggingAuditTrail();

  @Test
  void shouldLogOperationWithAmount() {
    audit.record(new AuditEvent(AuditEventType.PAYMENT_MADE, ACCOUNT, Optional.of(amount("40")), balance("60"), NOW));

    logs.shouldHave(
      Level.INFO,
      "Audit PAYMENT_MADE on account 00000000-0000-4000-8000-000000000042: amount=40.00, balance=60.00, date=2026-03-10T09:00:00Z"
    );
  }

  @Test
  void shouldLogOperationWithoutAmount() {
    audit.record(new AuditEvent(AuditEventType.ACCOUNT_OPENED, ACCOUNT, Optional.empty(), Balance.ZERO, NOW));

    logs.shouldHave(Level.INFO, "Audit ACCOUNT_OPENED on account 00000000-0000-4000-8000-000000000042: amount=none, balance=0.00");
  }
}
