package fr.vmz.seed4j.sample.ai.advanced.leave.domain.balance;

import static fr.vmz.seed4j.sample.ai.advanced.leave.domain.employee.EmployeesFixture.*;
import static fr.vmz.seed4j.sample.ai.advanced.leave.domain.request.LeaveRequestsFixture.*;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import java.time.Year;
import org.junit.jupiter.api.Test;

@UnitTest
class LeaveBalanceTest {

  @Test
  void shouldGrant25DaysByDefault() {
    var balance = LeaveBalance.standard(EMMA.id(), Year.of(2026));

    assertThat(balance.employee()).isEqualTo(EMMA.id());
    assertThat(balance.year()).isEqualTo(Year.of(2026));
    assertThat(balance.entitlement()).isEqualTo(days("25"));
  }

  @Test
  void shouldChangeEntitlement() {
    var balance = LeaveBalance.standard(EMMA.id(), Year.of(2026));

    balance.changeEntitlement(days("27.5"));

    assertThat(balance.entitlement()).isEqualTo(days("27.5"));
  }
}
