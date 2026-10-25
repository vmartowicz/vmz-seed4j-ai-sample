package fr.vmz.seed4j.sample.ai.advanced.library.application;

import fr.vmz.seed4j.sample.ai.advanced.library.domain.book.BooksRepository;
import fr.vmz.seed4j.sample.ai.advanced.library.domain.book.Isbn;
import fr.vmz.seed4j.sample.ai.advanced.library.domain.loan.Loan;
import fr.vmz.seed4j.sample.ai.advanced.library.domain.loan.LoanId;
import fr.vmz.seed4j.sample.ai.advanced.library.domain.loan.LoansDesk;
import fr.vmz.seed4j.sample.ai.advanced.library.domain.loan.LoansRepository;
import fr.vmz.seed4j.sample.ai.advanced.library.domain.loan.MemberFines;
import fr.vmz.seed4j.sample.ai.advanced.library.domain.member.MemberId;
import fr.vmz.seed4j.sample.ai.advanced.library.domain.member.MembersRepository;
import java.time.Clock;
import java.time.LocalDate;
import org.springframework.stereotype.Service;

@Service
public class LoansApplicationService {

  private final LoansDesk desk;
  private final Clock clock;

  public LoansApplicationService(MembersRepository members, BooksRepository books, LoansRepository loans, Clock clock) {
    desk = new LoansDesk(members, books, loans);
    this.clock = clock;
  }

  public Loan borrow(MemberId member, Isbn book) {
    return desk.borrow(member, book, LocalDate.now(clock));
  }

  public Loan returnBook(LoanId loan) {
    return desk.returnBook(loan, LocalDate.now(clock));
  }

  public MemberFines finesOf(MemberId member) {
    return desk.finesOf(member);
  }
}
