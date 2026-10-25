package fr.vmz.seed4j.sample.ai.advanced.leave.domain.request;

import static fr.vmz.seed4j.sample.ai.advanced.leave.domain.employee.EmployeesFixture.*;
import static fr.vmz.seed4j.sample.ai.advanced.leave.domain.request.LeaveRequestsFixture.*;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.shared.error.domain.MissingMandatoryValueException;
import java.util.Optional;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@UnitTest
class LeaveRequestTest {

  @Test
  void shouldCreatePendingRequest() {
    var request = pending(EMMA, fullDay(MONDAY));

    assertThat(request.id()).isNotNull();
    assertThat(request.employee()).isEqualTo(EMMA.id());
    assertThat(request.period()).isEqualTo(fullDay(MONDAY));
    assertThat(request.requestedOn()).isEqualTo(TODAY);
    assertThat(request.status()).isEqualTo(LeaveRequestStatus.PENDING);
    assertThat(request.rejectionComment()).isEmpty();
    assertThat(request.isActive()).isTrue();
  }

  @Test
  void shouldFlagRequestMade14DaysBeforeAsShortNotice() {
    assertThat(pending(EMMA, fullDay(TODAY.plusDays(14))).shortNotice()).isTrue();
  }

  @Test
  void shouldNotFlagRequestMade15DaysBeforeAsShortNotice() {
    assertThat(pending(EMMA, fullDay(TODAY.plusDays(15))).shortNotice()).isFalse();
  }

  @Nested
  class Decisions {

    @Test
    void shouldApproveRequest() {
      var request = pending(EMMA, fullDay(MONDAY));

      request.approve();

      assertThat(request.status()).isEqualTo(LeaveRequestStatus.APPROVED);
      assertThat(request.isActive()).isTrue();
    }

    @Test
    void shouldRejectRequestWithComment() {
      var request = pending(EMMA, fullDay(MONDAY));

      request.reject(Optional.of(new RejectionComment("Release week")));

      assertThat(request.status()).isEqualTo(LeaveRequestStatus.REJECTED);
      assertThat(request.rejectionComment()).contains(new RejectionComment("Release week"));
      assertThat(request.isActive()).isFalse();
    }

    @Test
    void shouldCancelRequest() {
      var request = pending(EMMA, fullDay(MONDAY));

      request.cancel();

      assertThat(request.status()).isEqualTo(LeaveRequestStatus.CANCELLED);
      assertThat(request.isActive()).isFalse();
    }

    @Test
    void shouldNotDecideTwice() {
      var request = approved(EMMA, fullDay(MONDAY));

      assertThatThrownBy(request::approve)
        .isExactlyInstanceOf(LeaveRequestNotPendingException.class)
        .hasMessageContaining("APPROVED");
      assertThatThrownBy(() -> request.reject(Optional.empty())).isExactlyInstanceOf(LeaveRequestNotPendingException.class);
      assertThatThrownBy(request::cancel).isExactlyInstanceOf(LeaveRequestNotPendingException.class);
    }
  }

  @Test
  void shouldNotBuildBlankRejectionComment() {
    assertThatThrownBy(() -> new RejectionComment(" ")).isExactlyInstanceOf(MissingMandatoryValueException.class);
    assertThat(new RejectionComment("Release week").get()).isEqualTo("Release week");
  }
}
