package fr.vmz.seed4j.sample.ai.expert.rental.fleet.domain;

class UnknownVehicleException extends UnknownFleetElementException {

  UnknownVehicleException(LicencePlate plate) {
    super("Unknown vehicle " + plate.get());
  }
}
