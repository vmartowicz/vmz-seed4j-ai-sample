package fr.vmz.seed4j.sample.ai.expert.conference.onsite.domain;

class NotInRoomException extends OnsiteRuleViolationException {

  NotInRoomException(BadgeId badge) {
    super("Badge " + badge.get() + " is not in any room");
  }
}
