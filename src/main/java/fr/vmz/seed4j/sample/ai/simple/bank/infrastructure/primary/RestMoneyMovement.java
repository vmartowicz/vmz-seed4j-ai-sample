package fr.vmz.seed4j.sample.ai.simple.bank.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.simple.bank.domain.Amount;
import fr.vmz.seed4j.sample.ai.simple.bank.domain.Label;
import java.math.BigDecimal;
import java.util.Optional;
import org.jspecify.annotations.Nullable;

record RestMoneyMovement(@Nullable BigDecimal amount, @Nullable String label) {
  Amount toAmount() {
    return new Amount(amount);
  }

  Optional<Label> toLabel() {
    return Optional.ofNullable(label).map(Label::new);
  }
}
