package fr.vmz.seed4j.sample.ai.expert.bank.compliance.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.bank.compliance.application.ComplianceApplicationService;
import fr.vmz.seed4j.sample.ai.expert.bank.compliance.domain.TransferToScreen;
import fr.vmz.seed4j.sample.ai.shared.bank.domain.Iban;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.springframework.stereotype.Component;

@Component
public class JavaCompliance {

  private final ComplianceApplicationService compliance;

  public JavaCompliance(ComplianceApplicationService compliance) {
    this.compliance = compliance;
  }

  public String screen(String payer, String payeeName, BigDecimal amount, LocalDateTime payeeAddedAt) {
    return compliance.screen(new TransferToScreen(new Iban(payer), payeeName, amount, payeeAddedAt)).name();
  }
}
