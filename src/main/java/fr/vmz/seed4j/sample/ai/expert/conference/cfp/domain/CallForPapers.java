package fr.vmz.seed4j.sample.ai.expert.conference.cfp.domain;

import fr.vmz.seed4j.sample.ai.shared.conference.domain.Edition;
import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public class CallForPapers {

  private static final int MAX_PROPOSALS_PER_SPEAKER = 3;
  private static final Comparator<Proposal> RANKING = Comparator.comparing((Proposal proposal) -> proposal.average().orElse(BigDecimal.ZERO))
    .reversed()
    .thenComparing(proposal -> !proposal.byNewcomers())
    .thenComparing(Proposal::submittedAt);

  private final ProposalsRepository proposals;
  private final CommitteeMembersRepository members;
  private final SpeakerTickets tickets;
  private final ProgramSchedule program;
  private final SponsoredTalks sponsors;
  private final SpeakerNotifications notifications;

  public CallForPapers(
    ProposalsRepository proposals,
    CommitteeMembersRepository members,
    SpeakerTickets tickets,
    ProgramSchedule program,
    SponsoredTalks sponsors,
    SpeakerNotifications notifications
  ) {
    this.proposals = proposals;
    this.members = members;
    this.tickets = tickets;
    this.program = program;
    this.sponsors = sponsors;
    this.notifications = notifications;
  }

  public Proposal submit(ProposalToSubmit toSubmit, LocalDateTime now) {
    Assert.notNull("proposal", toSubmit);

    if (!Edition.isCallForPapersOpenOn(now.toLocalDate())) {
      throw new CallForPapersClosedException(now.toLocalDate());
    }

    var existing = proposals.all();
    toSubmit
      .speakers()
      .stream()
      .filter(speaker -> existing.stream().filter(proposal -> proposal.involves(speaker.email())).count() >= MAX_PROPOSALS_PER_SPEAKER)
      .findFirst()
      .ifPresent(speaker -> {
        throw new TooManyProposalsException(speaker.email());
      });

    var proposal = toSubmit.submit(now);
    proposals.save(proposal);

    return proposal;
  }

  public Proposal submitSponsored(ProposalToSubmit toSubmit, UUID sponsor, LocalDateTime now) {
    Assert.notNull("proposal", toSubmit);

    var proposal = toSubmit.submitSponsored(sponsor, now);
    if (!sponsors.claim(sponsor)) {
      throw new SponsoredTalkNotAllowedException(sponsor);
    }

    proposals.save(proposal);
    proposal.speakers().forEach(tickets::issueFor);

    return proposal;
  }

  public CommitteeMember register(CommitteeMemberToRegister member) {
    var registered = member.register();

    members.save(registered);

    return registered;
  }

  public Proposal rate(ProposalId id, CommitteeMemberId memberId, int score) {
    var proposal = proposals.proposal(id);

    proposal.rate(members.member(memberId), score);
    proposals.save(proposal);

    return proposal;
  }

  public List<Proposal> select(Places places, LocalDate today) {
    var decided = new ArrayList<Proposal>();

    for (var format : Format.values()) {
      var submitted = proposals.all().stream().filter(proposal -> proposal.format() == format && proposal.status() == ProposalStatus.SUBMITTED);
      var ranked = new ArrayList<Proposal>();
      submitted.forEach(proposal -> {
        if (proposal.isRanked()) {
          ranked.add(proposal);
        } else {
          proposal.reject();
          decided.add(proposal);
        }
      });

      ranked.sort(RANKING);
      for (var index = 0; index < ranked.size(); index++) {
        var proposal = ranked.get(index);
        if (index < places.of(format)) {
          accept(proposal, today);
        } else {
          proposal.waitlist();
        }
        decided.add(proposal);
      }
    }

    decided.forEach(proposals::save);
    return decided;
  }

  public Proposal confirm(ProposalId id, LocalDate today) {
    var proposal = proposals.proposal(id);

    proposal.confirm(today);
    proposals.save(proposal);
    proposal.speakers().forEach(tickets::issueFor);

    return proposal;
  }

  public Proposal withdraw(ProposalId id, LocalDate today) {
    var proposal = proposals.proposal(id);

    var wasConfirmed = proposal.withdraw();
    proposals.save(proposal);
    program.withdrawn(proposal.id());
    if (wasConfirmed) {
      proposal
        .speakers()
        .stream()
        .filter(speaker -> proposals.all().stream().noneMatch(other -> other.isConfirmed() && other.involves(speaker.email())))
        .forEach(speaker -> tickets.revokeFor(speaker.email()));
    }

    replace(proposal.format(), today);
    return proposal;
  }

  public void expireUnconfirmed(LocalDate today) {
    proposals
      .all()
      .stream()
      .filter(proposal -> proposal.expireIfUnconfirmed(today))
      .toList()
      .forEach(expired -> {
        proposals.save(expired);
        notifyAll(expired, "Your proposal \"" + expired.title() + "\" expired: it was not confirmed within 10 days");
        replace(expired.format(), today);
      });
  }

  public List<Proposal> waitingList(Format format) {
    return waiting(format).toList();
  }

  public Proposal proposal(ProposalId id) {
    return proposals.proposal(id);
  }

  public Optional<Proposal> confirmedTalk(ProposalId id) {
    return proposals.get(id).filter(Proposal::isConfirmed);
  }

  public boolean isConfirmedWorkshop(UUID id) {
    return confirmedTalk(new ProposalId(id)).filter(proposal -> proposal.format() == Format.WORKSHOP).isPresent();
  }

  private void replace(Format format, LocalDate today) {
    waiting(format)
      .findFirst()
      .ifPresent(next -> {
        accept(next, today);
        proposals.save(next);
      });
  }

  private Stream<Proposal> waiting(Format format) {
    return proposals
      .all()
      .stream()
      .filter(proposal -> proposal.format() == format && proposal.status() == ProposalStatus.WAITING_LIST)
      .sorted(RANKING);
  }

  private void accept(Proposal proposal, LocalDate today) {
    proposal.accept(today);
    notifyAll(proposal, "Your proposal \"" + proposal.title() + "\" is accepted: please confirm it before " + proposal.confirmationDeadline().orElseThrow());
  }

  private void notifyAll(Proposal proposal, String message) {
    proposal.speakers().forEach(speaker -> notifications.notify(speaker.email(), message));
  }
}
