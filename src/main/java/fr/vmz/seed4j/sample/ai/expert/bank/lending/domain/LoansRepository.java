package fr.vmz.seed4j.sample.ai.expert.bank.lending.domain;

import java.util.List;
import java.util.Optional;

public interface LoansRepository {
  void save(Loan loan);

  Optional<Loan> get(LoanId loan);

  List<Loan> all();

  default Loan loan(LoanId loan) {
    return get(loan).orElseThrow(() -> new UnknownLoanException(loan));
  }
}
