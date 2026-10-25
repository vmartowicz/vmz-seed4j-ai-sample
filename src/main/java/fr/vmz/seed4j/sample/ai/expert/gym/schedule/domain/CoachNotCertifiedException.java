package fr.vmz.seed4j.sample.ai.expert.gym.schedule.domain;

class CoachNotCertifiedException extends ScheduleRuleViolationException {

  CoachNotCertifiedException(Coach coach, Discipline discipline) {
    super("Coach " + coach.name() + " is not certified in " + discipline);
  }
}
