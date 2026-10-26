package fr.vmz.seed4j.sample.ai.expert.medical.appointments.domain;

class InvalidSocialSecurityNumberException extends InvalidAppointmentsValueException {

  InvalidSocialSecurityNumberException(String number) {
    super("Invalid social security number (13 digits and a 2 digits key): " + number);
  }
}
