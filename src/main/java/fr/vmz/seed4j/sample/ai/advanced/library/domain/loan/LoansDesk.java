package fr.vmz.seed4j.sample.ai.advanced.library.domain.loan;

import fr.vmz.seed4j.sample.ai.advanced.library.domain.book.BooksRepository;
import fr.vmz.seed4j.sample.ai.advanced.library.domain.book.Isbn;
import fr.vmz.seed4j.sample.ai.advanced.library.domain.member.MemberId;
import fr.vmz.seed4j.sample.ai.advanced.library.domain.member.MembersRepository;
import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;

public class LoansDesk {

  private static final int MAX_ONGOING_LOANS = 3;
  private static final Fine SUSPENSION_THRESHOLD = new Fine(BigDecimal.TEN);
  private static final Comparator<Loan> BY_RETURN_DATE = Comparator.comparing(loan -> loan.returnedOn().orElseThrow());

  private final MembersRepository members;
  private final BooksRepository books;
  private final LoansRepository loans;

  public LoansDesk(MembersRepository members, BooksRepository books, LoansRepository loans) {
    this.members = members;
    this.books = books;
    this.loans = loans;
  }

  public Loan borrow(MemberId memberId, Isbn isbn, LocalDate today) {
    Assert.notNull("today", today);

    var member = members.member(memberId);
    var book = books.book(isbn);
    if (!member.isActive()) {
      throw new MemberSuspendedException(member.id());
    }

    var ongoingLoans = loans.of(member.id()).stream().filter(Loan::isOngoing).toList();
    if (ongoingLoans.stream().anyMatch(loan -> loan.isOverdueOn(today))) {
      throw new OverdueLoansException(member.id());
    }

    if (ongoingLoans.size() >= MAX_ONGOING_LOANS) {
      throw new BorrowingLimitReachedException(member.id());
    }

    book.borrow();
    var loan = new LoanToCreate(member.id(), book.isbn(), today).create();
    books.save(book);
    loans.save(loan);

    return loan;
  }

  public Loan returnBook(LoanId loanId, LocalDate today) {
    var loan = loans.loan(loanId);
    var book = books.book(loan.book());
    var member = members.member(loan.member());

    loan.giveBack(today);
    book.giveBack();
    loans.save(loan);
    books.save(book);

    if (finesOf(member.id()).total().exceeds(SUSPENSION_THRESHOLD)) {
      member.suspend();
      members.save(member);
    }

    return loan;
  }

  public MemberFines finesOf(MemberId member) {
    var lateReturns = loans
      .of(members.member(member).id())
      .stream()
      .filter(loan -> loan.fine().isDue())
      .sorted(BY_RETURN_DATE)
      .toList();

    return new MemberFines(lateReturns);
  }
}
