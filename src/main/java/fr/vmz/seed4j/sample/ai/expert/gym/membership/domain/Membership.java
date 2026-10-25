package fr.vmz.seed4j.sample.ai.expert.gym.membership.domain;

import fr.vmz.seed4j.sample.ai.shared.gym.domain.Gym;
import fr.vmz.seed4j.sample.ai.shared.gym.domain.MemberId;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class Membership {

  private static final Set<Month> FREE_SIGN_UP_MONTHS = Set.of(Month.JANUARY, Month.SEPTEMBER);
  private static final BigDecimal SIGN_UP_FEE = new BigDecimal("39");
  private static final int COMMITMENT_MONTHS = 12;

  private final MemberId id;
  private final String fullName;
  private final Plan plan;
  private final Gym homeGym;
  private final boolean committed;
  private final BigDecimal monthlyFee;
  private final LocalDate subscribedOn;
  private final List<Suspension> suspensions = new ArrayList<>();
  private MembershipStatus status;

  Membership(MembershipToSubscribe subscription, LocalDate subscribedOn) {
    id = MemberId.newId();
    fullName = subscription.fullName().trim();
    plan = subscription.plan();
    homeGym = subscription.homeGym();
    committed = subscription.committed();
    monthlyFee = subscription.monthlyFee();
    this.subscribedOn = subscribedOn;
    status = MembershipStatus.PENDING_PAYMENT;
  }

  public void activate() {
    if (status != MembershipStatus.PENDING_PAYMENT) {
      throw new InvalidMembershipTransitionException(id, status, "activate");
    }

    status = MembershipStatus.ACTIVE;
  }

  public void suspend(Suspension suspension, LocalDate today) {
    if (status != MembershipStatus.ACTIVE) {
      throw new InvalidMembershipTransitionException(id, status, "suspend");
    }

    if (suspension.start().isBefore(today)) {
      throw new SuspensionInThePastException(suspension.start());
    }

    if (suspensions.stream().anyMatch(previous -> previous.year().equals(suspension.year()))) {
      throw new SuspensionAlreadyUsedException(id, suspension.year());
    }

    suspensions.add(suspension);
  }

  public void terminate() {
    if (status == MembershipStatus.TERMINATED) {
      throw new InvalidMembershipTransitionException(id, status, "terminate");
    }

    status = MembershipStatus.TERMINATED;
  }

  public MembershipStatus statusOn(LocalDate date) {
    if (status == MembershipStatus.ACTIVE && isSuspendedOn(date)) {
      return MembershipStatus.SUSPENDED;
    }

    return status;
  }

  public boolean isSuspendedOn(LocalDate date) {
    return suspensions.stream().anyMatch(suspension -> suspension.covers(date));
  }

  public Optional<LocalDate> commitmentEnd() {
    if (!committed) {
      return Optional.empty();
    }

    var suspendedMonths = suspensions.stream().mapToInt(Suspension::months).sum();
    return Optional.of(subscribedOn.plusMonths(COMMITMENT_MONTHS + suspendedMonths));
  }

  public BigDecimal signUpFee() {
    return FREE_SIGN_UP_MONTHS.contains(subscribedOn.getMonth()) ? BigDecimal.ZERO : SIGN_UP_FEE;
  }

  public Set<Gym> accessibleGyms() {
    return plan == Plan.PREMIUM ? EnumSet.allOf(Gym.class) : EnumSet.of(homeGym);
  }

  public MemberId id() {
    return id;
  }

  public String fullName() {
    return fullName;
  }

  public Plan plan() {
    return plan;
  }

  public Gym homeGym() {
    return homeGym;
  }

  public boolean committed() {
    return committed;
  }

  public BigDecimal monthlyFee() {
    return monthlyFee;
  }

  public LocalDate subscribedOn() {
    return subscribedOn;
  }

  public MembershipStatus status() {
    return status;
  }
}
