package fr.vmz.seed4j.sample.ai.expert.rental.counter.domain;

class InvalidReturnMileageException extends CounterRuleViolationException {

  InvalidReturnMileageException(int mileageOut, int mileageIn) {
    super("The mileage at return (" + mileageIn + ") can not be below the mileage at pick-up (" + mileageOut + ")");
  }
}
