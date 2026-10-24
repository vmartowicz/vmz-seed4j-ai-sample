package fr.vmz.seed4j.sample.ai.simple.bank.domain;

class InsufficientBalanceException extends BankRuleViolationException {

  InsufficientBalanceException(AccountId account, Amount amount) {
    super("The balance of account " + account.get() + " can't cover a payment of " + amount.get());
  }
}
