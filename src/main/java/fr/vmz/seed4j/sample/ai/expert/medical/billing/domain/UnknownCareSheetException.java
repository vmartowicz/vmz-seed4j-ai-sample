package fr.vmz.seed4j.sample.ai.expert.medical.billing.domain;

class UnknownCareSheetException extends UnknownMedicalBillingElementException {

  UnknownCareSheetException(CareSheetId sheet) {
    super("Unknown care sheet " + sheet.get());
  }
}
