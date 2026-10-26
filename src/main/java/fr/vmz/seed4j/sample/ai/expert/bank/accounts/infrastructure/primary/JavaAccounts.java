package fr.vmz.seed4j.sample.ai.expert.bank.accounts.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.bank.accounts.application.RetailAccountsApplicationService;
import fr.vmz.seed4j.sample.ai.expert.bank.accounts.domain.AccountFrozenException;
import fr.vmz.seed4j.sample.ai.expert.bank.accounts.domain.InsufficientFundsException;
import fr.vmz.seed4j.sample.ai.shared.bank.domain.CustomerId;
import fr.vmz.seed4j.sample.ai.shared.bank.domain.Iban;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class JavaAccounts {

  private static final String DONE = "DONE";

  private final RetailAccountsApplicationService accounts;

  public JavaAccounts(RetailAccountsApplicationService accounts) {
    this.accounts = accounts;
  }

  public void freezeAccountsOf(UUID customer) {
    accounts.freezeAccountsOf(new CustomerId(customer));
  }

  public void unfreezeAccountsOf(UUID customer) {
    accounts.unfreezeAccountsOf(new CustomerId(customer));
  }

  public void freezeForSanctions(String iban) {
    accounts.freezeForSanctions(new Iban(iban));
  }

  public String debit(String iban, BigDecimal amount, String label) {
    return outcome(() -> accounts.debit(new Iban(iban), amount, label));
  }

  public String debitBeyondOverdraft(String iban, BigDecimal amount, String label) {
    return outcome(() -> accounts.debitBeyondOverdraft(new Iban(iban), amount, label));
  }

  public boolean collectForBank(String iban, BigDecimal amount, String label) {
    return DONE.equals(outcome(() -> accounts.collectForBank(new Iban(iban), amount, label)));
  }

  public void credit(String iban, BigDecimal amount, String label) {
    accounts.credit(new Iban(iban), amount, label);
  }

  public void chargeFee(String iban, BigDecimal amount, String label) {
    accounts.chargeFee(new Iban(iban), amount, label);
  }

  public boolean exists(String iban) {
    return accounts.exists(new Iban(iban));
  }

  public Optional<String> currentAccountOf(UUID customer) {
    return accounts.currentAccountOf(new CustomerId(customer)).map(Iban::get);
  }

  public boolean wasInUnauthorizedOverdraftSince(UUID customer, LocalDate date) {
    return accounts.wasInUnauthorizedOverdraftSince(new CustomerId(customer), date);
  }

  private static String outcome(Runnable movement) {
    try {
      movement.run();
      return DONE;
    } catch (AccountFrozenException _) {
      return "FROZEN";
    } catch (InsufficientFundsException _) {
      return "INSUFFICIENT_FUNDS";
    }
  }
}
