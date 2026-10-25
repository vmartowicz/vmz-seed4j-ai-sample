package fr.vmz.seed4j.sample.ai.advanced.leave.infrastructure.secondary.balance;

import static fr.vmz.seed4j.sample.ai.advanced.leave.domain.employee.EmployeesFixture.*;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.advanced.leave.domain.balance.LeaveBalance;
import java.time.Year;
import org.junit.jupiter.api.Test;

@UnitTest
class InMemoryLeaveBalancesRepositoryTest {

  private final InMemoryLeaveBalancesRepository balances = new InMemoryLeaveBalancesRepository();

  @Test
  void shouldGetBalanceOfEmployeeAndYear() {
    var balance = LeaveBalance.standard(EMMA.id(), Year.of(2026));
    balances.save(balance);

    assertThat(balances.get(EMMA.id(), Year.of(2026))).contains(balance);
    assertThat(balances.get(EMMA.id(), Year.of(2027))).isEmpty();
    assertThat(balances.get(LUCAS.id(), Year.of(2026))).isEmpty();
  }
}
