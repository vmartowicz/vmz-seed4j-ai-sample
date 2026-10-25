package fr.vmz.seed4j.sample.ai.expert.conference.cfp.application;

import fr.vmz.seed4j.sample.ai.expert.conference.cfp.domain.CallForPapers;
import fr.vmz.seed4j.sample.ai.expert.conference.cfp.domain.CommitteeMember;
import fr.vmz.seed4j.sample.ai.expert.conference.cfp.domain.CommitteeMemberId;
import fr.vmz.seed4j.sample.ai.expert.conference.cfp.domain.CommitteeMemberToRegister;
import fr.vmz.seed4j.sample.ai.expert.conference.cfp.domain.CommitteeMembersRepository;
import fr.vmz.seed4j.sample.ai.expert.conference.cfp.domain.Format;
import fr.vmz.seed4j.sample.ai.expert.conference.cfp.domain.Places;
import fr.vmz.seed4j.sample.ai.expert.conference.cfp.domain.ProgramSchedule;
import fr.vmz.seed4j.sample.ai.expert.conference.cfp.domain.Proposal;
import fr.vmz.seed4j.sample.ai.expert.conference.cfp.domain.ProposalId;
import fr.vmz.seed4j.sample.ai.expert.conference.cfp.domain.ProposalToSubmit;
import fr.vmz.seed4j.sample.ai.expert.conference.cfp.domain.ProposalsRepository;
import fr.vmz.seed4j.sample.ai.expert.conference.cfp.domain.SpeakerNotifications;
import fr.vmz.seed4j.sample.ai.expert.conference.cfp.domain.SpeakerTickets;
import fr.vmz.seed4j.sample.ai.expert.conference.cfp.domain.SponsoredTalks;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class CallForPapersApplicationService {

  private final CallForPapers cfp;
  private final Clock clock;

  public CallForPapersApplicationService(
    ProposalsRepository proposals,
    CommitteeMembersRepository members,
    SpeakerTickets tickets,
    ProgramSchedule program,
    SponsoredTalks sponsors,
    SpeakerNotifications notifications,
    Clock clock
  ) {
    cfp = new CallForPapers(proposals, members, tickets, program, sponsors, notifications);
    this.clock = clock;
  }

  public Proposal submit(ProposalToSubmit proposal) {
    return cfp.submit(proposal, LocalDateTime.now(clock));
  }

  public Proposal submitSponsored(ProposalToSubmit proposal, UUID sponsor) {
    return cfp.submitSponsored(proposal, sponsor, LocalDateTime.now(clock));
  }

  public Proposal proposal(ProposalId proposal) {
    return cfp.proposal(proposal);
  }

  public CommitteeMember register(CommitteeMemberToRegister member) {
    return cfp.register(member);
  }

  public Proposal rate(ProposalId proposal, CommitteeMemberId member, int score) {
    return cfp.rate(proposal, member, score);
  }

  public List<Proposal> select(Places places) {
    return cfp.select(places, today());
  }

  public List<Proposal> waitingList(Format format) {
    return cfp.waitingList(format);
  }

  public Proposal confirm(ProposalId proposal) {
    return cfp.confirm(proposal, today());
  }

  public Proposal withdraw(ProposalId proposal) {
    return cfp.withdraw(proposal, today());
  }

  public void expireUnconfirmed() {
    cfp.expireUnconfirmed(today());
  }

  public Optional<Proposal> confirmedTalk(ProposalId proposal) {
    return cfp.confirmedTalk(proposal);
  }

  public boolean isConfirmedWorkshop(UUID proposal) {
    return cfp.isConfirmedWorkshop(proposal);
  }

  private LocalDate today() {
    return LocalDate.now(clock);
  }
}
