package fr.vmz.seed4j.sample.ai.expert.bank.payments.domain;

import fr.vmz.seed4j.sample.ai.shared.bank.domain.Iban;
import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.math.BigDecimal;

public record TransferToOrder(Iban payer, PayeeId payee, BigDecimal amount, String reason, TransferKind kind) {
  private static final BigDecimal INSTANT_LIMIT = new BigDecimal("15000");

  public TransferToOrder {
    Assert.notNull("payer", payer);
    Assert.notNull("payee", payee);
    Assert.field("amount", amount).strictlyPositive();
    Assert.field("reason", reason).notBlank().maxLength(140);
    Assert.notNull("kind", kind);

    if (kind == TransferKind.INSTANT && amount.compareTo(INSTANT_LIMIT) > 0) {
      throw new InstantTransferLimitException(amount);
    }
  }
}
