package fr.vmz.seed4j.sample.ai.expert.bank.compliance.domain;

import fr.vmz.seed4j.sample.ai.shared.bank.domain.Iban;

public interface SuspiciousAccounts {
  void freeze(Iban account);
}
