package fr.vmz.seed4j.sample.ai.expert.bank.payments.domain;

import static fr.vmz.seed4j.sample.ai.expert.bank.payments.domain.PaymentsFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.shared.bank.domain.Iban;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@UnitTest
class PaymentsDeskTest {

  private final PayeesRepository payees = mock(PayeesRepository.class, CALLS_REAL_METHODS);
  private final TransfersRepository transfers = mock(TransfersRepository.class, CALLS_REAL_METHODS);
  private final MandatesRepository mandates = mock(MandatesRepository.class, CALLS_REAL_METHODS);
  private final DirectDebitsRepository debits = mock(DirectDebitsRepository.class, CALLS_REAL_METHODS);
  private final PaymentAccounts accounts = mock(PaymentAccounts.class);
  private final TransferScreening screening = mock(TransferScreening.class);
  private final PaymentsDesk desk = new PaymentsDesk(payees, transfers, mandates, debits, accounts, screening);

  private Payee payee;

  @BeforeEach
  void loadPayee() {
    payee = payee();
    doReturn(Optional.of(payee)).when(payees).get(payee.id());
    doReturn(true).when(accounts).exists(PAYER);
    doReturn(ScreeningResult.CLEAR).when(screening).screen(any(), anyString(), any(), any());
    doReturn(DebitOutcome.DONE).when(accounts).debit(any(), any(), anyString());
  }

  @Nested
  class Payees {

    @Test
    void shouldAddPayeeToExistingAccount() {
      var added = desk.addPayee(PAYER, "John Doe", Iban.generate(), MONDAY_MORNING);

      assertThat(added.payer()).isEqualTo(PAYER);
      assertThat(added.addedAt()).isEqualTo(MONDAY_MORNING);
      verify(payees).save(added);
    }

    @Test
    void shouldNotAddPayeeToUnknownAccount() {
      var unknown = Iban.generate();

      assertThatThrownBy(() -> desk.addPayee(unknown, "John Doe", Iban.generate(), MONDAY_MORNING)).isExactlyInstanceOf(UnknownPayerAccountException.class);
    }
  }

  @Nested
  class Transfers {

    @Test
    void shouldExecuteInstantTransferAndCreditOurPayee() {
      var ours = Payee.add(PAYER, "Our customer", Iban.generate(), MONDAY_MORNING.minusDays(3));
      doReturn(Optional.of(ours)).when(payees).get(ours.id());

      var transfer = desk.order(order(ours, "100", TransferKind.INSTANT), MONDAY_MORNING);

      assertThat(transfer.status()).isEqualTo(TransferStatus.EXECUTED);
      verify(accounts).debit(PAYER, euros("100"), "Transfer to Our customer: Rent");
      verify(accounts).credit(ours.iban(), euros("100"), "Transfer from " + PAYER.get() + ": Rent");
      verify(transfers).save(transfer);
    }

    @Test
    void shouldNotCreditPayeeOfAnotherBank() {
      var external = Payee.add(PAYER, "Other bank", new Iban("FR7630006000011234567890189"), MONDAY_MORNING.minusDays(3));
      doReturn(Optional.of(external)).when(payees).get(external.id());

      desk.order(order(external, "100", TransferKind.INSTANT), MONDAY_MORNING);

      verify(accounts, never()).credit(any(), any(), anyString());
    }

    @Test
    void shouldScheduleStandardTransfer() {
      var transfer = desk.order(order(payee, "100", TransferKind.STANDARD), MONDAY_MORNING);

      assertThat(transfer.status()).isEqualTo(TransferStatus.SCHEDULED);
      verify(accounts, never()).debit(any(), any(), anyString());
    }

    @Test
    void shouldRejectTransferOfFrozenOrUnderfundedAccount() {
      doReturn(DebitOutcome.FROZEN).when(accounts).debit(any(), any(), anyString());
      assertThat(desk.order(order(payee, "100", TransferKind.INSTANT), MONDAY_MORNING).rejection()).contains("The account is frozen");

      doReturn(DebitOutcome.INSUFFICIENT_FUNDS).when(accounts).debit(any(), any(), anyString());
      assertThat(desk.order(order(payee, "100", TransferKind.INSTANT), MONDAY_MORNING).rejection()).contains("Insufficient funds");
    }

    @Test
    void shouldBlockOrHoldScreenedTransfers() {
      doReturn(ScreeningResult.BLOCK).when(screening).screen(eq(PAYER), eq("Jane Doe"), any(), eq(payee.addedAt()));
      assertThat(desk.order(order(payee, "100", TransferKind.INSTANT), MONDAY_MORNING).status()).isEqualTo(TransferStatus.BLOCKED);

      doReturn(ScreeningResult.HOLD).when(screening).screen(any(), anyString(), any(), any());
      assertThat(desk.order(order(payee, "100", TransferKind.INSTANT), MONDAY_MORNING).status()).isEqualTo(TransferStatus.HELD);
      verify(accounts, never()).debit(any(), any(), anyString());
    }

    @Test
    void shouldNotOrderTransferToPayeeOfAnotherAccount() {
      var otherPayee = Payee.add(Iban.generate(), "Jane Doe", Iban.generate(), MONDAY_MORNING);
      doReturn(Optional.of(otherPayee)).when(payees).get(otherPayee.id());

      assertThatThrownBy(() -> desk.order(order(otherPayee, "100", TransferKind.INSTANT), MONDAY_MORNING)).isExactlyInstanceOf(UnknownPayeeException.class);
    }

    @Test
    void shouldReleaseHeldTransfers() {
      doReturn(ScreeningResult.HOLD).when(screening).screen(any(), anyString(), any(), any());
      var standard = known(desk.order(order(payee, "20000", TransferKind.STANDARD), MONDAY_MORNING));
      var instant = known(desk.order(order(payee, "12000", TransferKind.INSTANT), MONDAY_MORNING));

      desk.release(instant.id(), MONDAY_MORNING.plusHours(1));
      desk.release(standard.id(), MONDAY_MORNING.plusHours(1));

      assertThat(instant.status()).isEqualTo(TransferStatus.EXECUTED);
      assertThat(standard.status()).isEqualTo(TransferStatus.SCHEDULED);
    }

    @Test
    void shouldExecuteDueTransfers() {
      var due = desk.order(order(payee, "100", TransferKind.STANDARD), MONDAY_MORNING);
      var later = desk.order(order(payee, "100", TransferKind.STANDARD), MONDAY_MORNING.plusDays(1));
      doReturn(List.of(due, later)).when(transfers).all();

      desk.executeDue(LocalDate.of(2026, 6, 2).atTime(7, 0));

      assertThat(due.status()).isEqualTo(TransferStatus.EXECUTED);
      assertThat(later.status()).isEqualTo(TransferStatus.SCHEDULED);
    }

    @Test
    void shouldNotFindUnknownTransfer() {
      var unknown = TransferId.newId();
      doReturn(Optional.empty()).when(transfers).get(unknown);

      assertThatThrownBy(() -> desk.transfer(unknown)).isExactlyInstanceOf(UnknownTransferException.class);
    }

    private Transfer known(Transfer transfer) {
      doReturn(Optional.of(transfer)).when(transfers).get(transfer.id());

      return transfer;
    }
  }

  @Nested
  class DirectDebits {

    private Mandate mandate;

    @BeforeEach
    void signMandate() {
      mandate = desk.signMandate(PAYER, "EDF", MONDAY_MORNING.toLocalDate());
      doReturn(Optional.of(mandate)).when(mandates).get(mandate.id());
    }

    @Test
    void shouldCollectDirectDebitBeyondOverdraft() {
      doReturn(DebitOutcome.DONE).when(accounts).directDebit(any(), any(), anyString());

      var debit = desk.collect(mandate.id(), euros("80"), MONDAY_MORNING);

      assertThat(debit.status()).isEqualTo(DirectDebitStatus.COLLECTED);
      verify(accounts).directDebit(PAYER, euros("80"), "Direct debit EDF");
      verify(debits).save(debit);
    }

    @Test
    void shouldCharge20EurosForRejectedDirectDebit() {
      doReturn(DebitOutcome.FROZEN).when(accounts).directDebit(any(), any(), anyString());

      var debit = desk.collect(mandate.id(), euros("80"), MONDAY_MORNING);

      assertThat(debit.status()).isEqualTo(DirectDebitStatus.REJECTED);
      verify(accounts).chargeFee(PAYER, euros("20"), "Rejected direct debit EDF");
    }

    @Test
    void shouldNotCollectRevokedMandate() {
      desk.revoke(mandate.id());

      assertThatThrownBy(() -> desk.collect(mandate.id(), euros("80"), MONDAY_MORNING)).isExactlyInstanceOf(MandateNotActiveException.class);
    }

    @Test
    void shouldRefundDirectDebit() {
      doReturn(DebitOutcome.DONE).when(accounts).directDebit(any(), any(), anyString());
      var debit = desk.collect(mandate.id(), euros("80"), MONDAY_MORNING);
      doReturn(Optional.of(debit)).when(debits).get(debit.id());

      desk.refund(debit.id(), MONDAY_MORNING.plusWeeks(2));

      verify(accounts).credit(PAYER, euros("80"), "Refund of direct debit EDF");
    }

    @Test
    void shouldNotUseUnknownMandateOrDirectDebit() {
      var unknownMandate = MandateId.newId();
      doReturn(Optional.empty()).when(mandates).get(unknownMandate);
      var unknownDebit = DirectDebitId.newId();
      doReturn(Optional.empty()).when(debits).get(unknownDebit);

      assertThatThrownBy(() -> desk.revoke(unknownMandate)).isExactlyInstanceOf(UnknownMandateException.class);
      assertThatThrownBy(() -> desk.refund(unknownDebit, MONDAY_MORNING)).isExactlyInstanceOf(UnknownDirectDebitException.class);
    }

    @Test
    void shouldNotSignMandateOnUnknownAccount() {
      assertThatThrownBy(() -> desk.signMandate(Iban.generate(), "EDF", MONDAY_MORNING.toLocalDate())).isExactlyInstanceOf(UnknownPayerAccountException.class);
    }
  }
}
