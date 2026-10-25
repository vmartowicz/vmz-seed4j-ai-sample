package fr.vmz.seed4j.sample.ai.expert.conference.cfp.domain;

import fr.vmz.seed4j.sample.ai.shared.conference.domain.Email;
import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.jspecify.annotations.Nullable;

public final class Proposal {

  private static final int RATINGS_TO_RANK = 3;
  private static final int CONFIRMATION_DAYS = 10;
  private static final Set<ProposalStatus> ACCEPTABLE = Set.of(ProposalStatus.SUBMITTED, ProposalStatus.WAITING_LIST);
  private static final Set<ProposalStatus> WITHDRAWABLE = Set.of(ProposalStatus.ACCEPTED, ProposalStatus.CONFIRMED);

  private final ProposalId id;
  private final String title;
  private final String summary;
  private final Format format;
  private final Level level;
  private final Track track;
  private final List<SpeakerProfile> speakers;
  private final LocalDateTime submittedAt;
  private final @Nullable UUID sponsor;
  private final Map<CommitteeMemberId, Integer> ratings = new HashMap<>();
  private ProposalStatus status;
  private @Nullable LocalDate acceptedOn;

  Proposal(ProposalToSubmit proposal, LocalDateTime submittedAt, @Nullable UUID sponsor) {
    id = ProposalId.newId();
    title = proposal.title().trim();
    summary = proposal.summary().trim();
    format = proposal.format();
    level = proposal.level();
    track = proposal.track();
    speakers = proposal.speakers();
    this.submittedAt = submittedAt;
    this.sponsor = sponsor;
    status = sponsor == null ? ProposalStatus.SUBMITTED : ProposalStatus.CONFIRMED;
  }

  public void rate(CommitteeMember member, int score) {
    assertStatus(Set.of(ProposalStatus.SUBMITTED), "rate");
    Assert.field("score", score).min(1).max(5);
    if (speakers.stream().anyMatch(member::worksWith)) {
      throw new ConflictOfInterestException(member);
    }

    ratings.put(member.id(), score);
  }

  public void accept(LocalDate today) {
    assertStatus(ACCEPTABLE, "accept");

    status = ProposalStatus.ACCEPTED;
    acceptedOn = today;
  }

  public void waitlist() {
    assertStatus(Set.of(ProposalStatus.SUBMITTED), "waitlist");

    status = ProposalStatus.WAITING_LIST;
  }

  public void reject() {
    assertStatus(Set.of(ProposalStatus.SUBMITTED), "reject");

    status = ProposalStatus.REJECTED;
  }

  public void confirm(LocalDate today) {
    assertStatus(Set.of(ProposalStatus.ACCEPTED), "confirm");
    var deadline = confirmationDeadline().orElseThrow();
    if (today.isAfter(deadline)) {
      throw new ConfirmationDeadlinePassedException(id, deadline);
    }

    status = ProposalStatus.CONFIRMED;
  }

  public boolean expireIfUnconfirmed(LocalDate today) {
    if (status != ProposalStatus.ACCEPTED || !today.isAfter(confirmationDeadline().orElseThrow())) {
      return false;
    }

    status = ProposalStatus.EXPIRED;
    return true;
  }

  public boolean withdraw() {
    assertStatus(WITHDRAWABLE, "withdraw");

    var wasConfirmed = isConfirmed();
    status = ProposalStatus.WITHDRAWN;
    return wasConfirmed;
  }

  public Optional<BigDecimal> average() {
    if (ratings.isEmpty()) {
      return Optional.empty();
    }

    var total = ratings.values().stream().mapToInt(Integer::intValue).sum();
    return Optional.of(BigDecimal.valueOf(total).divide(BigDecimal.valueOf(ratings.size()), 2, RoundingMode.HALF_UP));
  }

  public int ratings() {
    return ratings.size();
  }

  public boolean isRanked() {
    return ratings.size() >= RATINGS_TO_RANK;
  }

  public boolean byNewcomers() {
    return speakers.stream().noneMatch(SpeakerProfile::spokeBefore);
  }

  public boolean involves(Email speaker) {
    return speakers.stream().anyMatch(profile -> profile.email().equals(speaker));
  }

  public boolean isConfirmed() {
    return status == ProposalStatus.CONFIRMED;
  }

  public Optional<LocalDate> confirmationDeadline() {
    return Optional.ofNullable(acceptedOn).map(accepted -> accepted.plusDays(CONFIRMATION_DAYS));
  }

  private void assertStatus(Set<ProposalStatus> expected, String action) {
    if (!expected.contains(status)) {
      throw new InvalidProposalTransitionException(id, status, action);
    }
  }

  public ProposalId id() {
    return id;
  }

  public String title() {
    return title;
  }

  public String summary() {
    return summary;
  }

  public Format format() {
    return format;
  }

  public Level level() {
    return level;
  }

  public Track track() {
    return track;
  }

  public List<SpeakerProfile> speakers() {
    return speakers;
  }

  public LocalDateTime submittedAt() {
    return submittedAt;
  }

  public ProposalStatus status() {
    return status;
  }

  public Optional<UUID> sponsor() {
    return Optional.ofNullable(sponsor);
  }
}
