package fr.vmz.seed4j.sample.ai.expert.bank.compliance.infrastructure.primary;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.expert.bank.compliance.application.ComplianceApplicationService;
import fr.vmz.seed4j.sample.ai.expert.bank.compliance.domain.ScreeningDecision;
import fr.vmz.seed4j.sample.ai.shared.bank.domain.Iban;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

@UnitTest
class JavaComplianceTest {

  @Test
  void shouldScreenTransfer() {
    var compliance = mock(ComplianceApplicationService.class);
    doReturn(ScreeningDecision.HOLD).when(compliance).screen(any());
    var payer = Iban.generate();

    assertThat(new JavaCompliance(compliance).screen(payer.get(), "Jane Doe", BigDecimal.TEN, LocalDateTime.of(2026, 6, 1, 10, 0))).isEqualTo("HOLD");
    verify(compliance).screen(argThat(transfer -> transfer.payer().equals(payer) && transfer.payeeName().equals("Jane Doe")));
  }
}
