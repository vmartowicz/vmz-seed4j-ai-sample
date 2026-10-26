package fr.vmz.seed4j.sample.ai.expert.bank.lending.application;

import fr.vmz.seed4j.sample.ai.expert.bank.lending.domain.Borrowers;
import fr.vmz.seed4j.sample.ai.expert.bank.lending.domain.Loan;
import fr.vmz.seed4j.sample.ai.expert.bank.lending.domain.LoanAccounts;
import fr.vmz.seed4j.sample.ai.expert.bank.lending.domain.LoanId;
import fr.vmz.seed4j.sample.ai.expert.bank.lending.domain.LoanOffice;
import fr.vmz.seed4j.sample.ai.expert.bank.lending.domain.LoanRequest;
import fr.vmz.seed4j.sample.ai.expert.bank.lending.domain.LoansRepository;
import java.time.Clock;
import java.time.LocalDate;
import org.springframework.stereotype.Service;

@Service
public class LendingApplicationService {

  private final LoanOffice office;
  private final Clock clock;

  public LendingApplicationService(LoansRepository loans, Borrowers borrowers, LoanAccounts accounts, Clock clock) {
    office = new LoanOffice(loans, borrowers, accounts);
    this.clock = clock;
  }

  public Loan apply(LoanRequest request) {
    return office.apply(request, today());
  }

  public Loan loan(LoanId loan) {
    return office.loan(loan);
  }

  public void disburse() {
    office.disburse(today());
  }

  public void collectInstalments() {
    office.collectInstalments(today());
  }

  private LocalDate today() {
    return LocalDate.now(clock);
  }
}
