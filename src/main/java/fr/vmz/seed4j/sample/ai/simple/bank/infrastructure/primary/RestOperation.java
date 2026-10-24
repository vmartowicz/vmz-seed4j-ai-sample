package fr.vmz.seed4j.sample.ai.simple.bank.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.simple.bank.domain.Label;
import fr.vmz.seed4j.sample.ai.simple.bank.domain.Operation;
import java.math.BigDecimal;
import java.time.Instant;
import org.jspecify.annotations.Nullable;

record RestOperation(String type, BigDecimal amount, @Nullable String label, Instant date, BigDecimal balanceAfter) {
  static RestOperation from(Operation operation) {
    return new RestOperation(
      operation.type().name(),
      operation.amount().get(),
      operation.label().map(Label::get).orElse(null),
      operation.date(),
      operation.balanceAfter().get()
    );
  }
}
