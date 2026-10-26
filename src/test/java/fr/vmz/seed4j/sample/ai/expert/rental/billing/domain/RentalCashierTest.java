package fr.vmz.seed4j.sample.ai.expert.rental.billing.domain;

import static fr.vmz.seed4j.sample.ai.expert.rental.billing.domain.RentalBillingFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.shared.rental.domain.Category;
import fr.vmz.seed4j.sample.ai.shared.rental.domain.RentalPoint;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;

@UnitTest
class RentalCashierTest {

  private final FinalInvoicesRepository invoices = mock(FinalInvoicesRepository.class, CALLS_REAL_METHODS);
  private final DepositsRepository deposits = mock(DepositsRepository.class, CALLS_REAL_METHODS);
  private final LoyaltyPoints loyalty = mock(LoyaltyPoints.class);
  private final RentalCashier cashier = new RentalCashier(invoices, deposits, loyalty);

  @Test
  void shouldHoldDeposit() {
    var contract = UUID.randomUUID();

    var deposit = cashier.holdDeposit(contract, JANE, new BigDecimal("1500"));

    assertThat(deposit.contract()).isEqualTo(contract);
    verify(deposits).save(deposit);
  }

  @Test
  void shouldCreditPointsOfSettledInvoice() {
    var report = onTime();
    var deposit = known(new Deposit(report.contract(), JANE, new BigDecimal("800")));

    var invoice = cashier.invoice(report, RETURN_DAY);

    assertThat(deposit.returnedOn()).contains(RETURN_DAY);
    verify(invoices).save(invoice);
    verify(deposits).save(deposit);
    verify(loyalty).credit(JANE, 105, report.contract().toString());
  }

  @Test
  void shouldCreditPointsOncePaid() {
    var report = report(Category.ECO, PLANNED_RETURN.plusMinutes(70), 600, RentalPoint.PART_DIEU, BigDecimal.ZERO, false, true);
    var deposit = known(new Deposit(report.contract(), JANE, new BigDecimal("800")));
    var invoice = cashier.invoice(report, RETURN_DAY);
    verifyNoInteractions(loyalty);
    doReturn(Optional.of(invoice)).when(invoices).get(invoice.id());

    cashier.pay(invoice.id());

    assertThat(invoice.status()).isEqualTo(FinalInvoiceStatus.PAID);
    assertThat(deposit.releaseIfDue(RETURN_DAY.plusDays(7))).isFalse();
    verify(loyalty).credit(JANE, 135, report.contract().toString());
  }

  @Test
  void shouldReleaseDueDeposits() {
    var due = returned(false);
    var notYet = returned(true);
    doReturn(List.of(due, notYet)).when(deposits).all();

    cashier.releaseDeposits(RETURN_DAY.plusDays(7));

    assertThat(due.status()).isEqualTo(DepositStatus.RELEASED);
    verify(deposits).save(due);
    verify(deposits, never()).save(notYet);
  }

  @Test
  void shouldRetainDamageQuote() {
    var deposit = known(returned(true));

    cashier.retainForDamage(deposit.contract(), new BigDecimal("450"), RETURN_DAY);

    assertThat(cashier.deposit(deposit.contract()).retained()).isEqualByComparingTo("450");
    verify(deposits).save(deposit);
  }

  @Test
  void shouldNotFindUnknownInvoiceOrDeposit() {
    var unknown = UUID.randomUUID();
    doReturn(Optional.empty()).when(deposits).get(unknown);
    doReturn(Optional.empty()).when(invoices).get(any());

    assertThatThrownBy(() -> cashier.deposit(unknown)).isExactlyInstanceOf(UnknownDepositException.class);
    assertThatThrownBy(() -> cashier.pay(FinalInvoiceId.newId())).isExactlyInstanceOf(UnknownFinalInvoiceException.class);
    verify(loyalty, never()).credit(any(), anyInt(), anyString());
  }

  private Deposit known(Deposit deposit) {
    doReturn(Optional.of(deposit)).when(deposits).get(deposit.contract());

    return deposit;
  }
}
