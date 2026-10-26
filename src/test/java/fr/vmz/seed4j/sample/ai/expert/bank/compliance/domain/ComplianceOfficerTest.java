package fr.vmz.seed4j.sample.ai.expert.bank.compliance.domain;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.shared.bank.domain.Iban;
import fr.vmz.seed4j.sample.ai.shared.error.domain.AssertionException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@UnitTest
class ComplianceOfficerTest {

  private static final LocalDateTime NOW = LocalDateTime.of(2026, 6, 1, 10, 0);

  private final SanctionsRepository sanctions = mock(SanctionsRepository.class);
  private final AlertsRepository alerts = mock(AlertsRepository.class);
  private final SuspiciousAccounts accounts = mock(SuspiciousAccounts.class);
  private final ComplianceOfficer officer = new ComplianceOfficer(sanctions, alerts, accounts);

  private final Iban payer = Iban.generate();

  @BeforeEach
  void loadSanctions() {
    doReturn(List.of(new SanctionedName("Evil Corp"))).when(sanctions).all();
  }

  @Test
  void shouldClearUsualTransfer() {
    assertThat(officer.screen(transfer("Jane Doe", "500", NOW.minusDays(1)), NOW)).isEqualTo(ScreeningDecision.CLEAR);
    verifyNoInteractions(accounts, alerts);
  }

  @Test
  void shouldBlockTransferToSanctionedPayeeAndFreezeAccount() {
    var decision = officer.screen(transfer("  EVIL corp ", "10", NOW.minusYears(1)), NOW);

    assertThat(decision).isEqualTo(ScreeningDecision.BLOCK);
    verify(accounts).freeze(payer);
    verify(alerts).save(argThat(alert -> alert.account().equals(payer) && alert.reason().contains("Evil Corp") && alert.at().equals(NOW)));
  }

  @Test
  void shouldHoldBigTransferToNewPayeeFor24Hours() {
    assertThat(officer.screen(transfer("Jane Doe", "10000.01", NOW.minusHours(23)), NOW)).isEqualTo(ScreeningDecision.HOLD);
    assertThat(officer.screen(transfer("Jane Doe", "10000", NOW.minusHours(23)), NOW)).isEqualTo(ScreeningDecision.CLEAR);
    assertThat(officer.screen(transfer("Jane Doe", "20000", NOW.minusHours(24)), NOW)).isEqualTo(ScreeningDecision.CLEAR);
    verifyNoInteractions(accounts);
  }

  @Test
  void shouldAddSanctionedNames() {
    var name = officer.addSanction("Bad Guys Ltd");

    assertThat(name.get()).isEqualTo("Bad Guys Ltd");
    verify(sanctions).save(name);
  }

  @Test
  void shouldListAlerts() {
    var alert = new ComplianceAlert(payer, "reason", NOW);
    doReturn(List.of(alert)).when(alerts).all();

    assertThat(officer.alerts()).containsExactly(alert);
  }

  @Test
  void shouldNotSanctionBlankName() {
    assertThatThrownBy(() -> new SanctionedName(" ")).isInstanceOf(AssertionException.class);
    verify(sanctions, never()).save(any());
  }

  private TransferToScreen transfer(String payee, String amount, LocalDateTime payeeAddedAt) {
    return new TransferToScreen(payer, payee, new BigDecimal(amount), payeeAddedAt);
  }
}
