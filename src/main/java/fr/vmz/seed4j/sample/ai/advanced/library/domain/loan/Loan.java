package fr.vmz.seed4j.sample.ai.advanced.library.domain.loan;

import fr.vmz.seed4j.sample.ai.advanced.library.domain.book.Isbn;
import fr.vmz.seed4j.sample.ai.advanced.library.domain.member.MemberId;
import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import org.jspecify.annotations.Nullable;

public final class Loan {

  private static final int LOAN_DURATION_IN_DAYS = 14;

  private final LoanId id;
  private final MemberId member;
  private final Isbn book;
  private final LocalDate borrowedOn;
  private final LocalDate dueOn;
  private @Nullable LocalDate returnedOn;

  Loan(LoanId id, LoanToCreate loan) {
    this.id = id;
    member = loan.member();
    book = loan.book();
    borrowedOn = loan.borrowedOn();
    dueOn = borrowedOn.plusDays(LOAN_DURATION_IN_DAYS);
  }

  public Fine giveBack(LocalDate today) {
    Assert.notNull("today", today);

    if (!isOngoing()) {
      throw new LoanAlreadyReturnedException(id);
    }

    returnedOn = today;

    return fine();
  }

  public boolean isOngoing() {
    return returnedOn == null;
  }

  public boolean isOverdueOn(LocalDate today) {
    return isOngoing() && today.isAfter(dueOn);
  }

  public long lateDays() {
    return returnedOn().map(date -> Math.max(0, ChronoUnit.DAYS.between(dueOn, date))).orElse(0L);
  }

  public Fine fine() {
    return Fine.forLateDays(lateDays());
  }

  public LoanId id() {
    return id;
  }

  public MemberId member() {
    return member;
  }

  public Isbn book() {
    return book;
  }

  public LocalDate borrowedOn() {
    return borrowedOn;
  }

  public LocalDate dueOn() {
    return dueOn;
  }

  public Optional<LocalDate> returnedOn() {
    return Optional.ofNullable(returnedOn);
  }
}
