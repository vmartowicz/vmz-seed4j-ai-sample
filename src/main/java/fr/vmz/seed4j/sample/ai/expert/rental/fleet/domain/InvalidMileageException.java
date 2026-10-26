package fr.vmz.seed4j.sample.ai.expert.rental.fleet.domain;

class InvalidMileageException extends FleetRuleViolationException {

  InvalidMileageException(LicencePlate plate, int mileage) {
    super("Vehicle " + plate.get() + " already has more than " + mileage + " km");
  }
}
