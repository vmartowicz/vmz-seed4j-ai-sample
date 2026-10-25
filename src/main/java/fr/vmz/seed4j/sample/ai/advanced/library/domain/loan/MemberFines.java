package fr.vmz.seed4j.sample.ai.advanced.library.domain.loan;

import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.util.List;

public record MemberFines(List<Loan> lateReturns) {
  public MemberFines {
    Assert.notNull("lateReturns", lateReturns);

    lateReturns = List.copyOf(lateReturns);
  }

  public Fine total() {
    return lateReturns.stream().map(Loan::fine).reduce(Fine.NONE, Fine::plus);
  }
}
