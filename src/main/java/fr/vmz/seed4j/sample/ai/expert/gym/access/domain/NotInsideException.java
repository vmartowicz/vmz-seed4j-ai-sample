package fr.vmz.seed4j.sample.ai.expert.gym.access.domain;

class NotInsideException extends AccessRuleViolationException {

  NotInsideException(BadgeNumber badge) {
    super("Badge " + badge.get() + " did not enter any gym");
  }
}
