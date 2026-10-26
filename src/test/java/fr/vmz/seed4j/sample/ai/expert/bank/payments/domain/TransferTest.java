package fr.vmz.seed4j.sample.ai.expert.bank.payments.domain;

import static fr.vmz.seed4j.sample.ai.expert.bank.payments.domain.PaymentsFixture.*;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.shared.error.domain.AssertionException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@UnitTest
class TransferTest {

  @ParameterizedTest
  @CsvSource(
    {
      "2026-06-01T10:00, 2026-06-02",
      "2026-06-01T15:59, 2026-06-02",
      "2026-06-01T16:00, 2026-06-03",
      "2026-06-05T15:00, 2026-06-08",
      "2026-06-05T17:00, 2026-06-09",
      "2026-06-06T09:00, 2026-06-08",
    }
  )
  void shouldExecuteStandardTransfersOnBusinessDays(LocalDateTime orderedAt, LocalDate execution) {
    assertThat(Transfer.executionDateFor(orderedAt)).isEqualTo(execution);
  }

  @Test
  void shouldScheduleStandardTransfer() {
    var payee = payee();

    var transfer = Transfer.order(order(payee, "100", TransferKind.STANDARD), payee, MONDAY_MORNING);

    assertThat(transfer.id()).isNotNull();
    assertThat(transfer.payer()).isEqualTo(PAYER);
    assertThat(transfer.payeeName()).isEqualTo("Jane Doe");
    assertThat(transfer.payeeIban()).isEqualTo(payee.iban());
    assertThat(transfer.amount()).isEqualByComparingTo("100");
    assertThat(transfer.reason()).isEqualTo("Rent");
    assertThat(transfer.kind()).isEqualTo(TransferKind.STANDARD);
    assertThat(transfer.orderedAt()).isEqualTo(MONDAY_MORNING);
    assertThat(transfer.executionDate()).isEqualTo(LocalDate.of(2026, 6, 2));
    assertThat(transfer.status()).isEqualTo(TransferStatus.SCHEDULED);
    assertThat(transfer.isDueOn(LocalDate.of(2026, 6, 1))).isFalse();
    assertThat(transfer.isDueOn(LocalDate.of(2026, 6, 2))).isTrue();
  }

  @Test
  void shouldExecuteInstantTransferRightAway() {
    var transfer = Transfer.order(order(payee(), "15000", TransferKind.INSTANT), payee(), MONDAY_MORNING);

    assertThat(transfer.executionDate()).isEqualTo(MONDAY_MORNING.toLocalDate());
  }

  @Test
  void shouldNotOrderInstantTransferAbove15000() {
    assertThatThrownBy(() -> order(payee(), "15000.01", TransferKind.INSTANT)).isExactlyInstanceOf(InstantTransferLimitException.class);
  }

  @Test
  void shouldNotOrderTransferWithoutReason() {
    assertThatThrownBy(() -> new TransferToOrder(PAYER, payee().id(), euros("10"), " ", TransferKind.STANDARD)).isInstanceOf(AssertionException.class);
  }

  @Test
  void shouldFollowTransferLifecycle() {
    var held = Transfer.order(order(payee(), "100", TransferKind.STANDARD), payee(), MONDAY_MORNING);
    held.hold();
    assertThat(held.status()).isEqualTo(TransferStatus.HELD);
    assertThat(held.isDueOn(LocalDate.of(2026, 6, 30))).isFalse();

    held.release(MONDAY_MORNING.plusDays(1).withHour(17));

    assertThat(held.status()).isEqualTo(TransferStatus.SCHEDULED);
    assertThat(held.executionDate()).isEqualTo(LocalDate.of(2026, 6, 4));
    held.executed();
    assertThat(held.status()).isEqualTo(TransferStatus.EXECUTED);
    assertThatThrownBy(() -> held.release(MONDAY_MORNING)).isExactlyInstanceOf(TransferNotHeldException.class);
  }

  @Test
  void shouldKeepRejectionReason() {
    var transfer = Transfer.order(order(payee(), "100", TransferKind.INSTANT), payee(), MONDAY_MORNING);

    transfer.rejected("Insufficient funds");

    assertThat(transfer.status()).isEqualTo(TransferStatus.REJECTED);
    assertThat(transfer.rejection()).contains("Insufficient funds");
  }

  @Test
  void shouldBlockTransfer() {
    var transfer = Transfer.order(order(payee(), "100", TransferKind.INSTANT), payee(), MONDAY_MORNING);

    transfer.block();

    assertThat(transfer.status()).isEqualTo(TransferStatus.BLOCKED);
    assertThat(transfer.rejection()).isEmpty();
  }
}
