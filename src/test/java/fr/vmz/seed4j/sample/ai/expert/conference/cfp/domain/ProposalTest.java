package fr.vmz.seed4j.sample.ai.expert.conference.cfp.domain;

import static fr.vmz.seed4j.sample.ai.expert.conference.cfp.domain.ProposalsFixture.*;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.shared.conference.domain.Email;
import fr.vmz.seed4j.sample.ai.shared.error.domain.AssertionException;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@UnitTest
class ProposalTest {

  @Nested
  class Submission {

    @Test
    void shouldSubmitProposal() {
      var proposal = submitted();

      assertThat(proposal.id()).isNotNull();
      assertThat(proposal.title()).isEqualTo("Hexagonal for real");
      assertThat(proposal.summary()).isEqualTo("Ports, adapters and tests");
      assertThat(proposal.format()).isEqualTo(Format.TALK);
      assertThat(proposal.level()).isEqualTo(Level.INTERMEDIATE);
      assertThat(proposal.track()).isEqualTo(Track.CRAFT);
      assertThat(proposal.speakers()).containsExactly(jane());
      assertThat(proposal.submittedAt()).isEqualTo(SUBMITTED_AT);
      assertThat(proposal.status()).isEqualTo(ProposalStatus.SUBMITTED);
      assertThat(proposal.sponsor()).isEmpty();
      assertThat(proposal.average()).isEmpty();
      assertThat(proposal.involves(jane().email())).isTrue();
      assertThat(proposal.involves(new Email("john@acme.com"))).isFalse();
    }

    @Test
    void shouldSubmitWithTwoSpeakers() {
      assertThat(talkBy(jane(), speaker("john", "globex")).submit(SUBMITTED_AT).speakers()).hasSize(2);
    }

    @Test
    void shouldNotSubmitWithMoreThanTwoSpeakers() {
      assertThatThrownBy(() -> talkBy(jane(), speaker("john", "globex"), speaker("jim", "initech"))).isInstanceOf(AssertionException.class);
    }

    @Test
    void shouldNotSubmitWithoutSpeaker() {
      assertThatThrownBy(() -> talkBy()).isInstanceOf(AssertionException.class);
    }

    @Test
    void shouldNotSubmitWithTheSameSpeakerTwice() {
      assertThatThrownBy(() -> talkBy(jane(), jane())).isExactlyInstanceOf(DuplicatedSpeakerException.class);
    }

    @Test
    void shouldConfirmSponsoredTalkRightAway() {
      var sponsor = UUID.randomUUID();

      var proposal = talkBy(jane()).submitSponsored(sponsor, SUBMITTED_AT);

      assertThat(proposal.status()).isEqualTo(ProposalStatus.CONFIRMED);
      assertThat(proposal.sponsor()).contains(sponsor);
    }

    @ParameterizedTest
    @ValueSource(strings = { "QUICKIE", "WORKSHOP" })
    void shouldSponsorOnlyTalks(Format format) {
      var toSubmit = proposal(format, jane());

      assertThatThrownBy(() -> toSubmit.submitSponsored(UUID.randomUUID(), SUBMITTED_AT)).isExactlyInstanceOf(SponsoredTalkFormatException.class);
    }

    @Test
    void shouldNotDescribeSpeakerWithoutCompany() {
      assertThatThrownBy(() -> new SpeakerProfile(jane().email(), "Jane", " ", false, false)).isInstanceOf(AssertionException.class);
    }
  }

  @Nested
  class Rating {

    @Test
    void shouldAverageRatings() {
      var proposal = rated(5, 4, 4);

      assertThat(proposal.ratings()).isEqualTo(3);
      assertThat(proposal.isRanked()).isTrue();
      assertThat(proposal.average()).hasValueSatisfying(average -> assertThat(average).isEqualByComparingTo("4.33"));
    }

    @Test
    void shouldNotRankWithLessThan3Ratings() {
      assertThat(rated(5, 5).isRanked()).isFalse();
    }

    @Test
    void shouldReplaceRatingOfSameMember() {
      var proposal = submitted();
      var member = member("initech");

      proposal.rate(member, 1);
      proposal.rate(member, 5);

      assertThat(proposal.ratings()).isEqualTo(1);
      assertThat(proposal.average()).hasValueSatisfying(average -> assertThat(average).isEqualByComparingTo("5"));
    }

    @Test
    void shouldNotRateProposalOfOwnCompany() {
      var proposal = talkBy(speaker("john", "globex"), jane()).submit(SUBMITTED_AT);

      assertThatThrownBy(() -> proposal.rate(member("ACME"), 3)).isExactlyInstanceOf(ConflictOfInterestException.class);
    }

    @ParameterizedTest
    @ValueSource(ints = { 0, 6 })
    void shouldRateFrom1To5(int score) {
      var proposal = submitted();

      assertThatThrownBy(() -> proposal.rate(member("initech"), score)).isInstanceOf(AssertionException.class);
    }

    @Test
    void shouldNotRateDecidedProposal() {
      var proposal = accepted();

      assertThatThrownBy(() -> proposal.rate(member("initech"), 3)).isExactlyInstanceOf(InvalidProposalTransitionException.class);
    }

    @Test
    void shouldKnowNewcomers() {
      var veteran = new SpeakerProfile(new Email("old@globex.com"), "Old", "globex", false, true);

      assertThat(talkBy(jane()).submit(SUBMITTED_AT).byNewcomers()).isTrue();
      assertThat(talkBy(jane(), veteran).submit(SUBMITTED_AT).byNewcomers()).isFalse();
    }
  }

  @Nested
  class Decisions {

    @Test
    void shouldGive10DaysToConfirm() {
      var proposal = accepted();

      assertThat(proposal.status()).isEqualTo(ProposalStatus.ACCEPTED);
      assertThat(proposal.confirmationDeadline()).contains(SELECTION_DAY.plusDays(10));

      proposal.confirm(SELECTION_DAY.plusDays(10));

      assertThat(proposal.status()).isEqualTo(ProposalStatus.CONFIRMED);
      assertThat(proposal.isConfirmed()).isTrue();
    }

    @Test
    void shouldNotConfirmAfter10Days() {
      var proposal = accepted();

      assertThatThrownBy(() -> proposal.confirm(SELECTION_DAY.plusDays(11))).isExactlyInstanceOf(ConfirmationDeadlinePassedException.class);
    }

    @Test
    void shouldNotConfirmProposalNotAccepted() {
      assertThatThrownBy(() -> submitted().confirm(SELECTION_DAY)).isExactlyInstanceOf(InvalidProposalTransitionException.class);
    }

    @Test
    void shouldExpireUnconfirmedProposalAfter10Days() {
      var proposal = accepted();

      assertThat(proposal.expireIfUnconfirmed(SELECTION_DAY.plusDays(10))).isFalse();
      assertThat(proposal.expireIfUnconfirmed(SELECTION_DAY.plusDays(11))).isTrue();

      assertThat(proposal.status()).isEqualTo(ProposalStatus.EXPIRED);
    }

    @Test
    void shouldNotExpireOtherProposals() {
      assertThat(confirmed().expireIfUnconfirmed(SELECTION_DAY.plusYears(1))).isFalse();
      assertThat(submitted().expireIfUnconfirmed(SELECTION_DAY.plusYears(1))).isFalse();
    }

    @Test
    void shouldAcceptFromWaitingList() {
      var proposal = rated(3, 3, 3);
      proposal.waitlist();

      proposal.accept(SELECTION_DAY.plusDays(12));

      assertThat(proposal.confirmationDeadline()).contains(SELECTION_DAY.plusDays(22));
    }

    @Test
    void shouldRejectProposal() {
      var proposal = submitted();

      proposal.reject();

      assertThat(proposal.status()).isEqualTo(ProposalStatus.REJECTED);
      assertThatThrownBy(proposal::waitlist).isExactlyInstanceOf(InvalidProposalTransitionException.class);
      assertThatThrownBy(() -> proposal.accept(SELECTION_DAY)).isExactlyInstanceOf(InvalidProposalTransitionException.class);
    }

    @Test
    void shouldWithdrawConfirmedOrAcceptedProposal() {
      var confirmedProposal = confirmed();
      var acceptedProposal = accepted();

      assertThat(confirmedProposal.withdraw()).isTrue();
      assertThat(acceptedProposal.withdraw()).isFalse();

      assertThat(confirmedProposal.status()).isEqualTo(ProposalStatus.WITHDRAWN);
      assertThat(acceptedProposal.status()).isEqualTo(ProposalStatus.WITHDRAWN);
    }

    @Test
    void shouldNotWithdrawSubmittedProposal() {
      assertThatThrownBy(() -> submitted().withdraw()).isExactlyInstanceOf(InvalidProposalTransitionException.class);
    }
  }

  @Test
  void shouldNotSubmitWithoutTitle() {
    assertThatThrownBy(() -> new ProposalToSubmit(" ", "Summary", Format.TALK, Level.EXPERT, Track.BACK, List.of(jane()))).isInstanceOf(
      AssertionException.class
    );
  }
}
