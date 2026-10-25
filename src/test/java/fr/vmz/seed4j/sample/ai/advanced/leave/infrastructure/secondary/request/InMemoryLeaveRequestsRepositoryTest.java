package fr.vmz.seed4j.sample.ai.advanced.leave.infrastructure.secondary.request;

import static fr.vmz.seed4j.sample.ai.advanced.leave.domain.employee.EmployeesFixture.*;
import static fr.vmz.seed4j.sample.ai.advanced.leave.domain.request.LeaveRequestsFixture.*;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.advanced.leave.domain.request.LeaveRequestId;
import java.util.List;
import org.junit.jupiter.api.Test;

@UnitTest
class InMemoryLeaveRequestsRepositoryTest {

  private final InMemoryLeaveRequestsRepository requests = new InMemoryLeaveRequestsRepository();

  @Test
  void shouldGetSavedRequest() {
    var request = pending(EMMA, fullDay(MONDAY));
    requests.save(request);

    assertThat(requests.get(request.id())).contains(request);
    assertThat(requests.get(LeaveRequestId.newId())).isEmpty();
  }

  @Test
  void shouldGetRequestsOfEmployees() {
    var emmas = pending(EMMA, fullDay(MONDAY));
    var lucas = pending(LUCAS, fullDay(MONDAY));
    var hugos = pending(HUGO, fullDay(MONDAY));
    requests.save(emmas);
    requests.save(lucas);
    requests.save(hugos);

    assertThat(requests.ofEmployee(EMMA.id())).containsExactly(emmas);
    assertThat(requests.ofEmployees(List.of(EMMA.id(), LUCAS.id()))).containsExactlyInAnyOrder(emmas, lucas);
  }
}
