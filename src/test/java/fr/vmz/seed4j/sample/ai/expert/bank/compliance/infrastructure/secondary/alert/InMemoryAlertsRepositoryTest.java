package fr.vmz.seed4j.sample.ai.expert.bank.compliance.infrastructure.secondary.alert;

import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.expert.bank.compliance.domain.ComplianceAlert;
import fr.vmz.seed4j.sample.ai.shared.bank.domain.Iban;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

@UnitTest
class InMemoryAlertsRepositoryTest {

  @Test
  void shouldListAlerts() {
    var alerts = new InMemoryAlertsRepository();
    var alert = new ComplianceAlert(Iban.generate(), "Sanctioned payee", LocalDateTime.of(2026, 6, 1, 10, 0));

    alerts.save(alert);

    assertThat(alerts.all()).containsExactly(alert);
  }
}
