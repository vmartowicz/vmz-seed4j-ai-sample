package fr.vmz.seed4j.sample.ai.expert.bank.compliance.infrastructure.secondary.accounts;

import fr.vmz.seed4j.sample.ai.expert.bank.accounts.infrastructure.primary.JavaAccounts;
import fr.vmz.seed4j.sample.ai.expert.bank.compliance.domain.SuspiciousAccounts;
import fr.vmz.seed4j.sample.ai.shared.bank.domain.Iban;
import org.springframework.stereotype.Component;

@Component
class ComplianceSuspiciousAccounts implements SuspiciousAccounts {

  private final JavaAccounts accounts;

  ComplianceSuspiciousAccounts(JavaAccounts accounts) {
    this.accounts = accounts;
  }

  @Override
  public void freeze(Iban account) {
    accounts.freezeForSanctions(account.get());
  }
}
