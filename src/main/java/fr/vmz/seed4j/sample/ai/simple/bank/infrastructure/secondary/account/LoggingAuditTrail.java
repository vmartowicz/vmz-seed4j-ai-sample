package fr.vmz.seed4j.sample.ai.simple.bank.infrastructure.secondary.account;

import fr.vmz.seed4j.sample.ai.simple.bank.domain.AuditEvent;
import fr.vmz.seed4j.sample.ai.simple.bank.domain.AuditTrail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
class LoggingAuditTrail implements AuditTrail {

  private static final Logger log = LoggerFactory.getLogger(LoggingAuditTrail.class);

  @Override
  public void record(AuditEvent event) {
    log.info(
      "Audit {} on account {}: amount={}, balance={}, date={}",
      event.type(),
      event.account().get(),
      event.amount().map(amount -> amount.get().toPlainString()).orElse("none"),
      event.balance().get().toPlainString(),
      event.date()
    );
  }
}
