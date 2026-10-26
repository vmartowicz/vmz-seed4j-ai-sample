package fr.vmz.seed4j.sample.ai.expert.bank.compliance.domain;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class ComplianceOfficer {

  private static final BigDecimal HOLD_THRESHOLD = new BigDecimal("10000");
  private static final int NEW_PAYEE_HOURS = 24;

  private final SanctionsRepository sanctions;
  private final AlertsRepository alerts;
  private final SuspiciousAccounts accounts;

  public ComplianceOfficer(SanctionsRepository sanctions, AlertsRepository alerts, SuspiciousAccounts accounts) {
    this.sanctions = sanctions;
    this.alerts = alerts;
    this.accounts = accounts;
  }

  public ScreeningDecision screen(TransferToScreen transfer, LocalDateTime now) {
    Assert.notNull("transfer", transfer);

    var sanctioned = sanctions.all().stream().filter(name -> name.matches(transfer.payeeName())).findFirst();
    if (sanctioned.isPresent()) {
      alerts.save(new ComplianceAlert(transfer.payer(), "Transfer to sanctioned payee " + sanctioned.orElseThrow().get(), now));
      accounts.freeze(transfer.payer());
      return ScreeningDecision.BLOCK;
    }

    if (transfer.amount().compareTo(HOLD_THRESHOLD) > 0 && transfer.payeeAddedAt().isAfter(now.minusHours(NEW_PAYEE_HOURS))) {
      return ScreeningDecision.HOLD;
    }

    return ScreeningDecision.CLEAR;
  }

  public SanctionedName addSanction(String name) {
    var sanctioned = new SanctionedName(name);

    sanctions.save(sanctioned);

    return sanctioned;
  }

  public List<ComplianceAlert> alerts() {
    return alerts.all();
  }
}
