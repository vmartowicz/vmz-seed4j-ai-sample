package fr.vmz.seed4j.sample.ai.expert.bank.payments.infrastructure.secondary.compliance;

import fr.vmz.seed4j.sample.ai.expert.bank.compliance.infrastructure.primary.JavaCompliance;
import fr.vmz.seed4j.sample.ai.expert.bank.payments.domain.ScreeningResult;
import fr.vmz.seed4j.sample.ai.expert.bank.payments.domain.TransferScreening;
import fr.vmz.seed4j.sample.ai.shared.bank.domain.Iban;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.springframework.stereotype.Component;

@Component
class PaymentsTransferScreening implements TransferScreening {

  private final JavaCompliance compliance;

  PaymentsTransferScreening(JavaCompliance compliance) {
    this.compliance = compliance;
  }

  @Override
  public ScreeningResult screen(Iban payer, String payeeName, BigDecimal amount, LocalDateTime payeeAddedAt) {
    return ScreeningResult.valueOf(compliance.screen(payer.get(), payeeName, amount, payeeAddedAt));
  }
}
