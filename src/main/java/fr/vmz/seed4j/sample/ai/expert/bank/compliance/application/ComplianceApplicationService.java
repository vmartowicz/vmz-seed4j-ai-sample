package fr.vmz.seed4j.sample.ai.expert.bank.compliance.application;

import fr.vmz.seed4j.sample.ai.expert.bank.compliance.domain.AlertsRepository;
import fr.vmz.seed4j.sample.ai.expert.bank.compliance.domain.ComplianceAlert;
import fr.vmz.seed4j.sample.ai.expert.bank.compliance.domain.ComplianceOfficer;
import fr.vmz.seed4j.sample.ai.expert.bank.compliance.domain.SanctionedName;
import fr.vmz.seed4j.sample.ai.expert.bank.compliance.domain.SanctionsRepository;
import fr.vmz.seed4j.sample.ai.expert.bank.compliance.domain.ScreeningDecision;
import fr.vmz.seed4j.sample.ai.expert.bank.compliance.domain.SuspiciousAccounts;
import fr.vmz.seed4j.sample.ai.expert.bank.compliance.domain.TransferToScreen;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ComplianceApplicationService {

  private final ComplianceOfficer officer;
  private final Clock clock;

  public ComplianceApplicationService(SanctionsRepository sanctions, AlertsRepository alerts, SuspiciousAccounts accounts, Clock clock) {
    officer = new ComplianceOfficer(sanctions, alerts, accounts);
    this.clock = clock;
  }

  public ScreeningDecision screen(TransferToScreen transfer) {
    return officer.screen(transfer, LocalDateTime.now(clock));
  }

  public SanctionedName addSanction(String name) {
    return officer.addSanction(name);
  }

  public List<ComplianceAlert> alerts() {
    return officer.alerts();
  }
}
