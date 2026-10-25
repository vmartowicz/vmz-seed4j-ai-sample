package fr.vmz.seed4j.sample.ai.expert.gym.schedule.domain;

import fr.vmz.seed4j.sample.ai.shared.gym.domain.Gym;

class UnknownStudioException extends UnknownScheduleElementException {

  UnknownStudioException(Gym gym, String studio) {
    super("No studio " + studio + " in " + gym);
  }
}
