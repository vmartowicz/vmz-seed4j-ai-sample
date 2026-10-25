package fr.vmz.seed4j.sample.ai.advanced.leave.domain.request;

import fr.vmz.seed4j.sample.ai.advanced.leave.domain.employee.EmployeeId;
import fr.vmz.seed4j.sample.ai.shared.error.domain.Assert;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import org.jspecify.annotations.Nullable;

public final class LeaveRequest {

  private static final int RECOMMENDED_NOTICE_IN_DAYS = 15;

  private final LeaveRequestId id;
  private final EmployeeId employee;
  private final LeavePeriod period;
  private final LocalDate requestedOn;
  private LeaveRequestStatus status = LeaveRequestStatus.PENDING;
  private @Nullable RejectionComment rejectionComment;

  LeaveRequest(LeaveRequestId id, LeaveRequestToCreate request) {
    this.id = id;
    employee = request.employee();
    period = request.period();
    requestedOn = request.requestedOn();
  }

  public void approve() {
    assertPending();

    status = LeaveRequestStatus.APPROVED;
  }

  public void reject(Optional<RejectionComment> comment) {
    Assert.notNull("comment", comment);
    assertPending();

    status = LeaveRequestStatus.REJECTED;
    rejectionComment = comment.orElse(null);
  }

  public void cancel() {
    assertPending();

    status = LeaveRequestStatus.CANCELLED;
  }

  public void assertPending() {
    if (status != LeaveRequestStatus.PENDING) {
      throw new LeaveRequestNotPendingException(id, status);
    }
  }

  public boolean isActive() {
    return status == LeaveRequestStatus.PENDING || status == LeaveRequestStatus.APPROVED;
  }

  public boolean shortNotice() {
    return ChronoUnit.DAYS.between(requestedOn, period.start().date()) < RECOMMENDED_NOTICE_IN_DAYS;
  }

  public LeaveRequestId id() {
    return id;
  }

  public EmployeeId employee() {
    return employee;
  }

  public LeavePeriod period() {
    return period;
  }

  public LocalDate requestedOn() {
    return requestedOn;
  }

  public LeaveRequestStatus status() {
    return status;
  }

  public Optional<RejectionComment> rejectionComment() {
    return Optional.ofNullable(rejectionComment);
  }
}
