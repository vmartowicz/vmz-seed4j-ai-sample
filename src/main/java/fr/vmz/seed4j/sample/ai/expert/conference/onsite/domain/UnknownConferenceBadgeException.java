package fr.vmz.seed4j.sample.ai.expert.conference.onsite.domain;

class UnknownConferenceBadgeException extends UnknownOnsiteElementException {

  UnknownConferenceBadgeException(BadgeId badge) {
    super("Unknown badge " + badge.get());
  }
}
