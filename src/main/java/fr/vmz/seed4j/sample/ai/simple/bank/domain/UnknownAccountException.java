package fr.vmz.seed4j.sample.ai.simple.bank.domain;

class UnknownAccountException extends UnknownBankElementException {

  UnknownAccountException(AccountId account) {
    super("Unknown account " + account.get());
  }
}
