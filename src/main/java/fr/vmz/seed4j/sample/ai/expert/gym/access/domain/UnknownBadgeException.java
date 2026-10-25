package fr.vmz.seed4j.sample.ai.expert.gym.access.domain;

class UnknownBadgeException extends UnknownAccessElementException {

  UnknownBadgeException(BadgeNumber badge) {
    super("Unknown badge " + badge.get());
  }
}
