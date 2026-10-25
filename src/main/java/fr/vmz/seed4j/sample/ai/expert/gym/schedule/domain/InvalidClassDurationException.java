package fr.vmz.seed4j.sample.ai.expert.gym.schedule.domain;

class InvalidClassDurationException extends InvalidScheduleValueException {

  InvalidClassDurationException(int minutes) {
    super("A class lasts 45 or 60 minutes, not " + minutes);
  }
}
