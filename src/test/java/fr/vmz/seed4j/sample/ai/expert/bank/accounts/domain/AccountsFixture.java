package fr.vmz.seed4j.sample.ai.expert.bank.accounts.domain;

import fr.vmz.seed4j.sample.ai.shared.bank.domain.CustomerId;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public final class AccountsFixture {

  public static final LocalDate TODAY = LocalDate.of(2026, 6, 1);
  public static final LocalDateTime NOW = TODAY.atTime(10, 0);

  private AccountsFixture() {}

  public static Holder adult(String income) {
    return new Holder(CustomerId.newId(), LocalDate.of(1990, 5, 4), new BigDecimal(income), false);
  }

  public static Holder minor() {
    return new Holder(CustomerId.newId(), TODAY.minusYears(14), BigDecimal.ZERO, true);
  }

  public static BankAccount current(String income) {
    return new AccountToOpen(AccountType.CURRENT, List.of(adult(income))).open(TODAY);
  }

  public static BankAccount currentWith(String balance) {
    var account = current("1500");
    account.credit(new BigDecimal(balance), "Salary", NOW);

    return account;
  }

  public static BankAccount savings() {
    return new AccountToOpen(AccountType.SAVINGS, List.of(adult("1500"))).open(TODAY);
  }

  public static BigDecimal euros(String amount) {
    return new BigDecimal(amount);
  }
}
