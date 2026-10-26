package fr.vmz.seed4j.sample.ai.expert.bank.payments.domain;

import fr.vmz.seed4j.sample.ai.shared.bank.domain.Iban;
import java.math.BigDecimal;

public interface PaymentAccounts {
  boolean exists(Iban account);

  DebitOutcome debit(Iban account, BigDecimal amount, String label);

  DebitOutcome directDebit(Iban account, BigDecimal amount, String label);

  void credit(Iban account, BigDecimal amount, String label);

  void chargeFee(Iban account, BigDecimal amount, String label);
}
