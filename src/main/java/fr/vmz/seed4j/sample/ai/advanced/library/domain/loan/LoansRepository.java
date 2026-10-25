package fr.vmz.seed4j.sample.ai.advanced.library.domain.loan;

import fr.vmz.seed4j.sample.ai.advanced.library.domain.member.MemberId;
import java.util.List;
import java.util.Optional;

public interface LoansRepository {
  void save(Loan loan);

  Optional<Loan> get(LoanId loan);

  List<Loan> of(MemberId member);

  default Loan loan(LoanId loan) {
    return get(loan).orElseThrow(() -> new UnknownLoanException(loan));
  }
}
