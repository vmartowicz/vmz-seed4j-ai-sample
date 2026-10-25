package fr.vmz.seed4j.sample.ai.advanced.library.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.advanced.library.application.LoansApplicationService;
import fr.vmz.seed4j.sample.ai.advanced.library.domain.loan.LoanId;
import fr.vmz.seed4j.sample.ai.advanced.library.domain.member.MemberId;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/library")
class LoansResource {

  private final LoansApplicationService loans;

  LoansResource(LoansApplicationService loans) {
    this.loans = loans;
  }

  @PostMapping("/loans")
  @ResponseStatus(HttpStatus.CREATED)
  RestLoan borrow(@RequestBody RestLoanToCreate loan) {
    return RestLoan.from(loans.borrow(loan.member(), loan.book()));
  }

  @PostMapping("/loans/{loanId}/return")
  RestLoan returnBook(@PathVariable UUID loanId) {
    return RestLoan.from(loans.returnBook(new LoanId(loanId)));
  }

  @GetMapping("/members/{memberId}/fines")
  RestMemberFines fines(@PathVariable UUID memberId) {
    return RestMemberFines.from(loans.finesOf(new MemberId(memberId)));
  }
}
