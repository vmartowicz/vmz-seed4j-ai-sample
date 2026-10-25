package fr.vmz.seed4j.sample.ai.advanced.leave.domain.request;

import static fr.vmz.seed4j.sample.ai.advanced.leave.domain.employee.EmployeesFixture.*;
import static fr.vmz.seed4j.sample.ai.advanced.leave.domain.request.LeaveRequestsFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.advanced.leave.domain.LeaveAccessDeniedException;
import fr.vmz.seed4j.sample.ai.advanced.leave.domain.employee.Employee;
import fr.vmz.seed4j.sample.ai.advanced.leave.domain.employee.EmployeesRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@UnitTest
class LeaveApproverTest {

  private static final List<Employee> DEVELOPMENT = List.of(MARC, EMMA, LUCAS, LEA, HUGO);

  private final EmployeesRepository employees = mock(EmployeesRepository.class, CALLS_REAL_METHODS);
  private final LeaveRequestsRepository requests = mock(LeaveRequestsRepository.class, CALLS_REAL_METHODS);
  private final LeaveNotifier notifier = mock(LeaveNotifier.class);
  private final LeaveApprover approver = new LeaveApprover(employees, requests, notifier);

  @BeforeEach
  void loadEmployees() {
    List.of(MARC, EMMA, LUCAS, LEA, HUGO, SOPHIE).forEach(employee -> doReturn(Optional.of(employee)).when(employees).get(employee.id()));
    doReturn(DEVELOPMENT).when(employees).membersOf(MARC.team());
    doReturn(List.of()).when(requests).ofEmployees(any());
  }

  @Nested
  class Approve {

    @Test
    void shouldApproveRequestOfTeamMember() {
      var request = existing(pending(EMMA, fullDay(MONDAY)));

      approver.approve(MARC.id(), request.id());

      assertThat(request.status()).isEqualTo(LeaveRequestStatus.APPROVED);
      verify(requests).save(request);
      verify(notifier).decisionMade(new LeaveDecision(EMMA.email(), request, Optional.empty()));
    }

    @Test
    void shouldNotApproveWithoutManagingTheEmployee() {
      var request = existing(pending(EMMA, fullDay(MONDAY)));

      assertThatThrownBy(() -> approver.approve(SOPHIE.id(), request.id()))
        .isExactlyInstanceOf(LeaveAccessDeniedException.class)
        .hasMessageContaining("Sophie");
      assertThatThrownBy(() -> approver.approve(LUCAS.id(), request.id())).isExactlyInstanceOf(LeaveAccessDeniedException.class);
      assertThat(request.status()).isEqualTo(LeaveRequestStatus.PENDING);
    }

    @Test
    void shouldApproveThirdSimultaneousLeaveOfTheTeam() {
      doReturn(List.of(approved(LUCAS, fullDay(MONDAY)), approved(LEA, fullDay(MONDAY)))).when(requests).ofEmployees(teamIds());
      var request = existing(pending(EMMA, fullDay(MONDAY)));

      approver.approve(MARC.id(), request.id());

      assertThat(request.status()).isEqualTo(LeaveRequestStatus.APPROVED);
    }

    @Test
    void shouldNotApproveFourthSimultaneousLeaveOfTheTeam() {
      var mondayAfternoon = new LeavePeriod(new DayPart(MONDAY, HalfDay.AFTERNOON), new DayPart(MONDAY, HalfDay.AFTERNOON));
      doReturn(
        List.of(approved(LUCAS, fullDay(MONDAY)), approved(LEA, fullDays(MONDAY, MONDAY.plusDays(1))), approved(HUGO, mondayAfternoon))
      )
        .when(requests)
        .ofEmployees(teamIds());
      var request = existing(pending(EMMA, fullDays(MONDAY, MONDAY.plusDays(1))));

      assertThatThrownBy(() -> approver.approve(MARC.id(), request.id()))
        .isExactlyInstanceOf(TeamLimitReachedException.class)
        .hasMessageContaining("2026-04-06");
      assertThat(request.status()).isEqualTo(LeaveRequestStatus.PENDING);
      verify(notifier, never()).decisionMade(any());
    }

    @Test
    void shouldIgnorePendingAndOtherDaysLeavesInTeamLimit() {
      doReturn(
        List.of(approved(LUCAS, fullDay(MONDAY)), approved(LEA, fullDay(MONDAY)), pending(HUGO, fullDay(MONDAY)), approved(HUGO, fullDay(MONDAY.plusDays(1))))
      )
        .when(requests)
        .ofEmployees(teamIds());
      var request = existing(pending(EMMA, fullDay(MONDAY)));

      approver.approve(MARC.id(), request.id());

      assertThat(request.status()).isEqualTo(LeaveRequestStatus.APPROVED);
    }

    @Test
    void shouldNotApproveDecidedRequest() {
      var request = existing(approved(EMMA, fullDay(MONDAY)));

      assertThatThrownBy(() -> approver.approve(MARC.id(), request.id())).isExactlyInstanceOf(LeaveRequestNotPendingException.class);
    }
  }

  @Test
  void shouldRejectRequestWithComment() {
    var request = existing(pending(EMMA, fullDay(MONDAY)));
    var comment = Optional.of(new RejectionComment("Release week"));

    approver.reject(MARC.id(), request.id(), comment);

    assertThat(request.status()).isEqualTo(LeaveRequestStatus.REJECTED);
    verify(requests).save(request);
    verify(notifier).decisionMade(new LeaveDecision(EMMA.email(), request, comment));
  }

  @Test
  void shouldNotRejectWithoutManagingTheEmployee() {
    var request = existing(pending(EMMA, fullDay(MONDAY)));

    assertThatThrownBy(() -> approver.reject(SOPHIE.id(), request.id(), Optional.empty())).isExactlyInstanceOf(
      LeaveAccessDeniedException.class
    );
  }

  @Test
  void shouldListPendingRequestsOfTheTeamToReview() {
    var later = pending(LUCAS, fullDay(MONDAY.plusDays(7)));
    var sooner = pending(EMMA, fullDay(MONDAY));
    var decided = approved(LEA, fullDay(MONDAY));
    var managersOwn = pending(MARC, fullDay(MONDAY));
    doReturn(List.of(later, sooner, decided, managersOwn)).when(requests).ofEmployees(teamIds());

    assertThat(approver.toReview(MARC.id())).containsExactly(sooner, later);
  }

  @Test
  void shouldNotListRequestsToReviewWithoutBeingManager() {
    assertThatThrownBy(() -> approver.toReview(EMMA.id())).isExactlyInstanceOf(LeaveAccessDeniedException.class);
  }

  private List<fr.vmz.seed4j.sample.ai.advanced.leave.domain.employee.EmployeeId> teamIds() {
    return DEVELOPMENT.stream().map(Employee::id).toList();
  }

  private LeaveRequest existing(LeaveRequest request) {
    doReturn(Optional.of(request)).when(requests).get(request.id());

    return request;
  }
}
