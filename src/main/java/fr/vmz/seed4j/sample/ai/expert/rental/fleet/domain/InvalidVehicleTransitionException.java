package fr.vmz.seed4j.sample.ai.expert.rental.fleet.domain;

class InvalidVehicleTransitionException extends FleetRuleViolationException {

  InvalidVehicleTransitionException(LicencePlate plate, VehicleStatus status, String action) {
    super("Can not " + action + " vehicle " + plate.get() + " which is " + status);
  }
}
