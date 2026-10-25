package fr.vmz.seed4j.sample.ai.expert.conference.onsite.domain;

class InactiveBadgeException extends OnsiteRuleViolationException {

  InactiveBadgeException(BadgeId badge) {
    super("Badge " + badge.get() + " was replaced by a new one");
  }
}
