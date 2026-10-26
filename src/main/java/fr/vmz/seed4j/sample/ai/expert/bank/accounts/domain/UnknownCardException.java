package fr.vmz.seed4j.sample.ai.expert.bank.accounts.domain;

class UnknownCardException extends UnknownRetailAccountsElementException {

  UnknownCardException(CardId card) {
    super("Unknown card " + card.get());
  }
}
