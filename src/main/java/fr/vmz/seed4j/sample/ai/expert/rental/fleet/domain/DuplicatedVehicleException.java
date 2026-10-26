package fr.vmz.seed4j.sample.ai.expert.rental.fleet.domain;

class DuplicatedVehicleException extends FleetRuleViolationException {

  DuplicatedVehicleException(LicencePlate plate) {
    super("Vehicle " + plate.get() + " is already in the fleet");
  }
}
