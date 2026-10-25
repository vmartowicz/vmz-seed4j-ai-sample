package fr.vmz.seed4j.sample.ai.advanced.library.infrastructure.primary;

import fr.vmz.seed4j.sample.ai.advanced.library.domain.loan.MemberFines;
import java.math.BigDecimal;
import java.util.List;

record RestMemberFines(List<RestFine> fines, BigDecimal total) {
  static RestMemberFines from(MemberFines fines) {
    return new RestMemberFines(fines.lateReturns().stream().map(RestFine::from).toList(), fines.total().get());
  }
}
