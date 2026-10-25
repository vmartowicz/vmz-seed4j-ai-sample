package fr.vmz.seed4j.sample.ai.expert.gym.billing.domain;

import fr.vmz.seed4j.sample.ai.shared.gym.domain.MemberId;
import java.time.LocalDate;
import java.util.Optional;
import org.jspecify.annotations.Nullable;

public final class BillingAccount {

  private static final int MONTHS_BEFORE_TERMINATION = 2;

  private final MemberId member;
  private final Money monthlyFee;
  private AccountStatus status;
  private @Nullable LocalDate arrearsSince;

  private BillingAccount(MemberId member, Money monthlyFee) {
    this.member = member;
    this.monthlyFee = monthlyFee;
    status = AccountStatus.AWAITING_FIRST_PAYMENT;
  }

  public static BillingAccount open(MemberId member, Money monthlyFee) {
    return new BillingAccount(member, monthlyFee);
  }

  public void firstPaymentReceived() {
    status = AccountStatus.UP_TO_DATE;
  }

  public void fallIntoArrears(LocalDate today) {
    if (isInArrears()) {
      return;
    }

    status = AccountStatus.IN_ARREARS;
    arrearsSince = today;
  }

  public void settle() {
    if (!isInArrears()) {
      throw new NotInArrearsException(member);
    }

    status = AccountStatus.UP_TO_DATE;
    arrearsSince = null;
  }

  public void close() {
    status = AccountStatus.CLOSED;
  }

  public boolean isBillable() {
    return status == AccountStatus.UP_TO_DATE || isInArrears();
  }

  public boolean isInArrears() {
    return status == AccountStatus.IN_ARREARS;
  }

  public boolean shouldBeTerminatedOn(LocalDate today) {
    return arrearsSince().map(since -> isInArrears() && !today.isBefore(since.plusMonths(MONTHS_BEFORE_TERMINATION))).orElse(false);
  }

  public MemberId member() {
    return member;
  }

  public Money monthlyFee() {
    return monthlyFee;
  }

  public AccountStatus status() {
    return status;
  }

  public Optional<LocalDate> arrearsSince() {
    return Optional.ofNullable(arrearsSince);
  }
}
