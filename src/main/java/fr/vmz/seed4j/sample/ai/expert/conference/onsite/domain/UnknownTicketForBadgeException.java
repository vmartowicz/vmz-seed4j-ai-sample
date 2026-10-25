package fr.vmz.seed4j.sample.ai.expert.conference.onsite.domain;

class UnknownTicketForBadgeException extends UnknownOnsiteElementException {

  UnknownTicketForBadgeException(String ticket) {
    super("Unknown ticket " + ticket);
  }
}
