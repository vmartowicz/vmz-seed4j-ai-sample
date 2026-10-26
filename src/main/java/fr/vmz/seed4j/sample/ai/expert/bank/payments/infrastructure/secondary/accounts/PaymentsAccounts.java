package fr.vmz.seed4j.sample.ai.expert.bank.payments.infrastructure.secondary.accounts;

import fr.vmz.seed4j.sample.ai.expert.bank.accounts.infrastructure.primary.JavaAccounts;
import fr.vmz.seed4j.sample.ai.expert.bank.payments.domain.DebitOutcome;
import fr.vmz.seed4j.sample.ai.expert.bank.payments.domain.PaymentAccounts;
import fr.vmz.seed4j.sample.ai.shared.bank.domain.Iban;
import java.math.BigDecimal;
import org.springframework.stereotype.Component;

@Component
class PaymentsAccounts implements PaymentAccounts {

  private final JavaAccounts accounts;

  PaymentsAccounts(JavaAccounts accounts) {
    this.accounts = accounts;
  }

  @Override
  public boolean exists(Iban account) {
    return accounts.exists(account.get());
  }

  @Override
  public DebitOutcome debit(Iban account, BigDecimal amount, String label) {
    return DebitOutcome.valueOf(accounts.debit(account.get(), amount, label));
  }

  @Override
  public DebitOutcome directDebit(Iban account, BigDecimal amount, String label) {
    return DebitOutcome.valueOf(accounts.debitBeyondOverdraft(account.get(), amount, label));
  }

  @Override
  public void credit(Iban account, BigDecimal amount, String label) {
    accounts.credit(account.get(), amount, label);
  }

  @Override
  public void chargeFee(Iban account, BigDecimal amount, String label) {
    accounts.chargeFee(account.get(), amount, label);
  }
}
