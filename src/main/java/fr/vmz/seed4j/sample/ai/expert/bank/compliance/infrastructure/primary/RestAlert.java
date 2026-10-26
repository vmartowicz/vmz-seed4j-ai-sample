package fr.vmz.seed4j.sample.ai.expert.bank.compliance.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.bank.compliance.domain.ComplianceAlert;
import java.time.LocalDateTime;

record RestAlert(String account, String reason, LocalDateTime at) {
  static RestAlert from(ComplianceAlert alert) {
    return new RestAlert(alert.account().get(), alert.reason(), alert.at());
  }
}
