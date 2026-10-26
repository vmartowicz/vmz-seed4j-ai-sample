package fr.vmz.seed4j.sample.ai.expert.bank.compliance.domain;

import fr.vmz.seed4j.sample.ai.shared.bank.domain.Iban;
import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransferToScreen(Iban payer, String payeeName, BigDecimal amount, LocalDateTime payeeAddedAt) {
  public TransferToScreen {
    Assert.notNull("payer", payer);
    Assert.notBlank("payeeName", payeeName);
    Assert.notNull("amount", amount);
    Assert.notNull("payeeAddedAt", payeeAddedAt);
  }
}
