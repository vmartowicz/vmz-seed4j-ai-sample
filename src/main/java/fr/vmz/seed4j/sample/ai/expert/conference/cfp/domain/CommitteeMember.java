package fr.vmz.seed4j.sample.ai.expert.conference.cfp.domain;

public record CommitteeMember(CommitteeMemberId id, String name, String company) {
  boolean worksWith(SpeakerProfile speaker) {
    return company.equalsIgnoreCase(speaker.company());
  }
}
