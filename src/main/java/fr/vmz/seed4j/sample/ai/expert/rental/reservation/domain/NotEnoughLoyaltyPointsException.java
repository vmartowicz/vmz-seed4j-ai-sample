package fr.vmz.seed4j.sample.ai.expert.rental.reservation.domain;

class NotEnoughLoyaltyPointsException extends RentalReservationRuleViolationException {

  NotEnoughLoyaltyPointsException(String customer) {
    super("Customer " + customer + " needs 500 loyalty points for a free day");
  }
}
