package fr.vmz.seed4j.sample.ai.expert.conference.cfp.domain;

import fr.vmz.seed4j.sample.ai.shared.conference.domain.Email;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public final class ProposalsFixture {

  public static final LocalDateTime SUBMITTED_AT = LocalDateTime.of(2026, 3, 1, 10, 0);
  public static final LocalDate SELECTION_DAY = LocalDate.of(2026, 4, 10);

  private ProposalsFixture() {}

  public static SpeakerProfile speaker(String name, String company) {
    return new SpeakerProfile(new Email(name + "@" + company + ".com"), name, company, false, false);
  }

  public static SpeakerProfile jane() {
    return speaker("jane", "acme");
  }

  public static ProposalToSubmit talkBy(SpeakerProfile... speakers) {
    return proposal(Format.TALK, speakers);
  }

  public static ProposalToSubmit proposal(Format format, SpeakerProfile... speakers) {
    return new ProposalToSubmit("Hexagonal for real", "Ports, adapters and tests", format, Level.INTERMEDIATE, Track.CRAFT, List.of(speakers));
  }

  public static Proposal submitted() {
    return talkBy(jane()).submit(SUBMITTED_AT);
  }

  public static Proposal rated(int... scores) {
    return rated(talkBy(jane()), SUBMITTED_AT, scores);
  }

  public static Proposal rated(ProposalToSubmit toSubmit, LocalDateTime at, int... scores) {
    var proposal = toSubmit.submit(at);
    for (var score : scores) {
      proposal.rate(member("company-" + UUID.randomUUID()), score);
    }

    return proposal;
  }

  public static CommitteeMember member(String company) {
    return new CommitteeMemberToRegister("Member of " + company, company).register();
  }

  public static Proposal accepted() {
    var proposal = rated(4, 4, 4);
    proposal.accept(SELECTION_DAY);

    return proposal;
  }

  public static Proposal confirmed() {
    var proposal = accepted();
    proposal.confirm(SELECTION_DAY);

    return proposal;
  }
}
