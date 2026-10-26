package fr.vmz.seed4j.sample.ai.expert.bank.lending.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.expert.bank.lending.application.LendingApplicationService;
import fr.vmz.seed4j.sample.ai.expert.bank.lending.domain.LoanId;
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
@RequestMapping("/api/retail-bank/loans")
class RetailLoansResource {

  private final LendingApplicationService lending;

  RetailLoansResource(LendingApplicationService lending) {
    this.lending = lending;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  RestLoan apply(@RequestBody RestLoanRequest request) {
    return RestLoan.from(lending.apply(request.toDomain()));
  }

  @GetMapping("/{loanId}")
  RestLoan loan(@PathVariable UUID loanId) {
    return RestLoan.from(lending.loan(new LoanId(loanId)));
  }
}
