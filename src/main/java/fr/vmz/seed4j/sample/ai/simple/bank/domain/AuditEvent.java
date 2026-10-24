package fr.vmz.seed4j.sample.ai.simple.bank.domain;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.time.Instant;
import java.util.Optional;

public record AuditEvent(AuditEventType type, AccountId account, Optional<Amount> amount, Balance balance, Instant date) {
  public AuditEvent {
    Assert.notNull("type", type);
    Assert.notNull("account", account);
    Assert.notNull("amount", amount);
    Assert.notNull("balance", balance);
    Assert.notNull("date", date);
  }
}
