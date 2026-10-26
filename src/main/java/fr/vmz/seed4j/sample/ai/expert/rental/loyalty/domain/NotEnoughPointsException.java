package fr.vmz.seed4j.sample.ai.expert.rental.loyalty.domain;

public class NotEnoughPointsException extends LoyaltyRuleViolationException {

  public NotEnoughPointsException(int balance) {
    super("A free day needs 500 points, the balance is " + balance);
  }
}
