package fr.vmz.seed4j.sample.ai.expert.bank.accounts.domain;

class CardNotActiveException extends RetailAccountsRuleViolationException {

  CardNotActiveException(CardId card, CardStatus status) {
    super("Card " + card.get() + " is " + status);
  }
}
