package fr.vmz.seed4j.sample.ai.expert.bank.accounts.domain;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record AccountToOpen(AccountType type, List<Holder> holders) {
  private static final BigDecimal MAX_OVERDRAFT = new BigDecimal("2000");

  public AccountToOpen {
    Assert.notNull("type", type);
    Assert.field("holders", holders).notEmpty().maxSize(2).noNullElement();

    if (holders.stream().map(Holder::id).distinct().count() != holders.size()) {
      throw new AccountNotAllowedException("A customer can not hold the same account twice");
    }

    var minors = holders.stream().anyMatch(Holder::minor);
    if (type == AccountType.YOUTH != (minors && holders.size() == 1)) {
      throw new AccountNotAllowedException("Youth accounts are for a single minor holder, and minors can only open youth accounts");
    }

    holders = List.copyOf(holders);
  }

  public BankAccount open(LocalDate today) {
    var overdraft = type == AccountType.CURRENT ? holders.getFirst().monthlyIncome().min(MAX_OVERDRAFT) : BigDecimal.ZERO;

    return new BankAccount(type, holders.stream().map(Holder::id).toList(), overdraft, today);
  }
}
