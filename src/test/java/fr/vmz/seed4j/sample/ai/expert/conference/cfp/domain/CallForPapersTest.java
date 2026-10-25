package fr.vmz.seed4j.sample.ai.expert.conference.cfp.domain;

import static fr.vmz.seed4j.sample.ai.expert.conference.cfp.domain.ProposalsFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@UnitTest
class CallForPapersTest {

  private final ProposalsRepository proposals = mock(ProposalsRepository.class, CALLS_REAL_METHODS);
  private final CommitteeMembersRepository members = mock(CommitteeMembersRepository.class, CALLS_REAL_METHODS);
  private final SpeakerTickets tickets = mock(SpeakerTickets.class);
  private final ProgramSchedule program = mock(ProgramSchedule.class);
  private final SponsoredTalks sponsors = mock(SponsoredTalks.class);
  private final SpeakerNotifications notifications = mock(SpeakerNotifications.class);
  private final CallForPapers cfp = new CallForPapers(proposals, members, tickets, program, sponsors, notifications);

  private final List<Proposal> all = new ArrayList<>();

  @BeforeEach
  void loadProposals() {
    doReturn(all).when(proposals).all();
  }

  @Nested
  class Submit {

    @Test
    void shouldSubmitDuringCallForPapers() {
      var proposal = cfp.submit(talkBy(jane()), SUBMITTED_AT);

      assertThat(proposal.status()).isEqualTo(ProposalStatus.SUBMITTED);
      verify(proposals).save(proposal);
    }

    @Test
    void shouldNotSubmitOutsideCallForPapers() {
      assertThatThrownBy(() -> cfp.submit(talkBy(jane()), LocalDateTime.of(2026, 4, 1, 0, 0))).isExactlyInstanceOf(
        CallForPapersClosedException.class
      );
    }

    @Test
    void shouldNotSubmitMoreThan3ProposalsPerSpeaker() {
      var coSpeaker = speaker("john", "globex");
      all.addAll(List.of(submitted(), talkBy(coSpeaker, jane()).submit(SUBMITTED_AT), talkBy(speaker("jim", "initech")).submit(SUBMITTED_AT)));
      assertThat(cfp.submit(talkBy(jane()), SUBMITTED_AT)).isNotNull();
      all.add(submitted());

      assertThatThrownBy(() -> cfp.submit(talkBy(coSpeaker, jane()), SUBMITTED_AT))
        .isExactlyInstanceOf(TooManyProposalsException.class)
        .hasMessageContaining("jane@acme.com");
    }

    @Test
    void shouldConfirmSponsoredTalkAndIssueSpeakerTickets() {
      var sponsor = UUID.randomUUID();
      doReturn(true).when(sponsors).claim(sponsor);

      var proposal = cfp.submitSponsored(talkBy(jane()), sponsor, LocalDateTime.of(2026, 9, 1, 10, 0));

      assertThat(proposal.isConfirmed()).isTrue();
      verify(proposals).save(proposal);
      verify(tickets).issueFor(jane());
    }

    @Test
    void shouldNotSubmitSponsoredTalkWithoutGoldRight() {
      var sponsor = UUID.randomUUID();

      assertThatThrownBy(() -> cfp.submitSponsored(talkBy(jane()), sponsor, SUBMITTED_AT)).isExactlyInstanceOf(
        SponsoredTalkNotAllowedException.class
      );
      verify(proposals, never()).save(any());
    }

    @Test
    void shouldCheckSponsoredFormatBeforeClaimingTheRight() {
      var sponsor = UUID.randomUUID();

      assertThatThrownBy(() -> cfp.submitSponsored(proposal(Format.WORKSHOP, jane()), sponsor, SUBMITTED_AT)).isExactlyInstanceOf(
        SponsoredTalkFormatException.class
      );
      verifyNoInteractions(sponsors);
    }
  }

  @Nested
  class Rate {

    @Test
    void shouldRateProposal() {
      var proposal = known(submitted());
      var member = knownMember(member("initech"));

      cfp.rate(proposal.id(), member.id(), 4);

      assertThat(proposal.ratings()).isEqualTo(1);
      verify(proposals).save(proposal);
    }

    @Test
    void shouldNotRateWithUnknownMember() {
      var proposal = known(submitted());
      var unknown = CommitteeMemberId.newId();
      doReturn(Optional.empty()).when(members).get(unknown);

      assertThatThrownBy(() -> cfp.rate(proposal.id(), unknown, 4)).isExactlyInstanceOf(UnknownCommitteeMemberException.class);
    }

    @Test
    void shouldRegisterCommitteeMember() {
      var member = cfp.register(new CommitteeMemberToRegister("Ada", "initech"));

      verify(members).save(member);
    }
  }

  @Nested
  class Select {

    @Test
    void shouldAcceptBestAveragesAndWaitlistTheNextOnes() {
      var best = rated(5, 5, 4);
      var second = rated(4, 4, 4);
      var third = rated(3, 3, 3);
      var unranked = rated(5, 5);
      var workshop = rated(proposal(Format.WORKSHOP, jane()), SUBMITTED_AT, 2, 2, 2);
      var alreadyConfirmed = confirmed();
      all.addAll(List.of(third, unranked, best, workshop, second, alreadyConfirmed));

      cfp.select(new Places(2, 0, 1), SELECTION_DAY);

      assertThat(best.status()).isEqualTo(ProposalStatus.ACCEPTED);
      assertThat(second.status()).isEqualTo(ProposalStatus.ACCEPTED);
      assertThat(third.status()).isEqualTo(ProposalStatus.WAITING_LIST);
      assertThat(unranked.status()).isEqualTo(ProposalStatus.REJECTED);
      assertThat(workshop.status()).isEqualTo(ProposalStatus.ACCEPTED);
      assertThat(alreadyConfirmed.status()).isEqualTo(ProposalStatus.CONFIRMED);
      verify(notifications, times(3)).notify(eq(jane().email()), contains("accepted"));
      verify(proposals, times(5)).save(any());
    }

    @Test
    void shouldFavourNewcomersThenEarlierProposalsOnTies() {
      var veteran = new SpeakerProfile(jane().email(), "Jane", "acme", false, true);
      var byVeteran = rated(talkBy(veteran), SUBMITTED_AT, 4, 4, 4);
      var late = rated(talkBy(speaker("late", "globex")), SUBMITTED_AT.plusDays(2), 4, 4, 4);
      var early = rated(talkBy(speaker("early", "globex")), SUBMITTED_AT.plusDays(1), 4, 4, 4);
      all.addAll(List.of(byVeteran, late, early));

      cfp.select(new Places(1, 0, 0), SELECTION_DAY);

      assertThat(early.status()).isEqualTo(ProposalStatus.ACCEPTED);
      assertThat(cfp.waitingList(Format.TALK)).containsExactly(late, byVeteran);
    }
  }

  @Nested
  class Confirm {

    @Test
    void shouldIssueTicketsToAllSpeakers() {
      var john = speaker("john", "globex");
      var proposal = rated(talkBy(jane(), john), SUBMITTED_AT, 4, 4, 4);
      proposal.accept(SELECTION_DAY);
      known(proposal);

      cfp.confirm(proposal.id(), SELECTION_DAY);

      assertThat(proposal.isConfirmed()).isTrue();
      verify(tickets).issueFor(jane());
      verify(tickets).issueFor(john);
      verify(proposals).save(proposal);
    }

    @Test
    void shouldNotConfirmUnknownProposal() {
      var unknown = ProposalId.newId();
      doReturn(Optional.empty()).when(proposals).get(unknown);

      assertThatThrownBy(() -> cfp.confirm(unknown, SELECTION_DAY)).isExactlyInstanceOf(UnknownProposalException.class);
    }
  }

  @Nested
  class Replacements {

    @Test
    void shouldReplaceWithdrawnTalkByFirstOfWaitingListOfSameFormat() {
      var withdrawn = known(confirmed());
      var next = waitlisted(Format.TALK, 4);
      var after = waitlisted(Format.TALK, 3);
      var workshop = waitlisted(Format.WORKSHOP, 5);
      var otherSpeakerTalk = rated(talkBy(speaker("john", "globex")), SUBMITTED_AT, 4, 4, 4);
      otherSpeakerTalk.accept(SELECTION_DAY);
      otherSpeakerTalk.confirm(SELECTION_DAY);
      all.addAll(List.of(withdrawn, after, workshop, next, otherSpeakerTalk));

      cfp.withdraw(withdrawn.id(), SELECTION_DAY.plusDays(20));

      assertThat(withdrawn.status()).isEqualTo(ProposalStatus.WITHDRAWN);
      assertThat(next.status()).isEqualTo(ProposalStatus.ACCEPTED);
      assertThat(next.confirmationDeadline()).contains(SELECTION_DAY.plusDays(30));
      assertThat(after.status()).isEqualTo(ProposalStatus.WAITING_LIST);
      assertThat(workshop.status()).isEqualTo(ProposalStatus.WAITING_LIST);
      verify(program).withdrawn(withdrawn.id());
      verify(tickets).revokeFor(jane().email());
    }

    @Test
    void shouldKeepTicketOfSpeakerWithAnotherConfirmedTalk() {
      var withdrawn = known(confirmed());
      all.addAll(List.of(withdrawn, confirmed()));

      cfp.withdraw(withdrawn.id(), SELECTION_DAY);

      verify(tickets, never()).revokeFor(any());
    }

    @Test
    void shouldNotRevokeTicketsWhenAcceptedTalkIsWithdrawn() {
      var withdrawn = known(accepted());
      all.add(withdrawn);

      cfp.withdraw(withdrawn.id(), SELECTION_DAY);

      verifyNoInteractions(tickets);
      verify(program).withdrawn(withdrawn.id());
    }

    @Test
    void shouldReplaceExpiredProposals() {
      var expired = accepted();
      var recent = rated(4, 4, 4);
      recent.accept(SELECTION_DAY.plusDays(5));
      var next = waitlisted(Format.TALK, 4);
      all.addAll(List.of(expired, recent, next));

      cfp.expireUnconfirmed(SELECTION_DAY.plusDays(11));

      assertThat(expired.status()).isEqualTo(ProposalStatus.EXPIRED);
      assertThat(recent.status()).isEqualTo(ProposalStatus.ACCEPTED);
      assertThat(next.status()).isEqualTo(ProposalStatus.ACCEPTED);
      verify(notifications).notify(eq(jane().email()), contains("expired"));
      verify(notifications).notify(eq(jane().email()), contains("accepted"));
      verify(notifications, times(2)).notify(any(), anyString());
    }

    @Test
    void shouldReplaceNothingWithoutWaitingList() {
      var withdrawn = known(confirmed());
      all.add(withdrawn);

      cfp.withdraw(withdrawn.id(), SELECTION_DAY);

      verify(notifications, never()).notify(any(), contains("accepted"));
    }

    private Proposal waitlisted(Format format, int score) {
      var proposal = rated(proposal(format, jane()), SUBMITTED_AT, score, score, score);
      proposal.waitlist();

      return proposal;
    }
  }

  @Test
  void shouldExposeConfirmedTalksOnly() {
    var confirmedTalk = known(confirmed());
    var acceptedTalk = known(accepted());
    var workshop = known(rated(proposal(Format.WORKSHOP, jane()), SUBMITTED_AT, 4, 4, 4));
    workshop.accept(SELECTION_DAY);
    workshop.confirm(SELECTION_DAY);
    var unknown = ProposalId.newId();
    doReturn(Optional.empty()).when(proposals).get(unknown);

    assertThat(cfp.confirmedTalk(confirmedTalk.id())).contains(confirmedTalk);
    assertThat(cfp.confirmedTalk(acceptedTalk.id())).isEmpty();
    assertThat(cfp.confirmedTalk(unknown)).isEmpty();
    assertThat(cfp.isConfirmedWorkshop(workshop.id().get())).isTrue();
    assertThat(cfp.isConfirmedWorkshop(confirmedTalk.id().get())).isFalse();
  }

  private Proposal known(Proposal proposal) {
    doReturn(Optional.of(proposal)).when(proposals).get(proposal.id());

    return proposal;
  }

  private CommitteeMember knownMember(CommitteeMember member) {
    doReturn(Optional.of(member)).when(members).get(member.id());

    return member;
  }
}
