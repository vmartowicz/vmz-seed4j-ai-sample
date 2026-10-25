package fr.vmz.seed4j.sample.ai.advanced.library.infrastructure.secondary.loan;

import fr.vmz.seed4j.sample.ai.advanced.library.domain.loan.Loan;
import fr.vmz.seed4j.sample.ai.advanced.library.domain.loan.LoanId;
import fr.vmz.seed4j.sample.ai.advanced.library.domain.loan.LoansRepository;
import fr.vmz.seed4j.sample.ai.advanced.library.domain.member.MemberId;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
class InMemoryLoansRepository implements LoansRepository {

  private final Map<LoanId, Loan> loans = new ConcurrentHashMap<>();

  @Override
  public void save(Loan loan) {
    loans.put(loan.id(), loan);
  }

  @Override
  public Optional<Loan> get(LoanId loan) {
    return Optional.ofNullable(loans.get(loan));
  }

  @Override
  public List<Loan> of(MemberId member) {
    return loans
      .values()
      .stream()
      .filter(loan -> loan.member().equals(member))
      .toList();
  }
}
