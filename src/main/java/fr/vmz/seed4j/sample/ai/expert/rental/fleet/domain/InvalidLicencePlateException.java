package fr.vmz.seed4j.sample.ai.expert.rental.fleet.domain;

class InvalidLicencePlateException extends InvalidFleetValueException {

  InvalidLicencePlateException(String plate) {
    super("Invalid licence plate (expected AA-123-AA): " + plate);
  }
}
