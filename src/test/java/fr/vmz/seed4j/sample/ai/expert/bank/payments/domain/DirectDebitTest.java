package fr.vmz.seed4j.sample.ai.expert.bank.payments.domain;

import static fr.vmz.seed4j.sample.ai.expert.bank.payments.domain.PaymentsFixture.*;
import static org.assertj.core.api.Assertions.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import org.junit.jupiter.api.Test;

@UnitTest
class DirectDebitTest {

  private final Mandate mandate = Mandate.sign(PAYER, "EDF", MONDAY_MORNING.toLocalDate());

  @Test
  void shouldSignActiveMandate() {
    assertThat(mandate.id()).isNotNull();
    assertThat(mandate.account()).isEqualTo(PAYER);
    assertThat(mandate.creditor()).isEqualTo("EDF");
    assertThat(mandate.signedOn()).isEqualTo(MONDAY_MORNING.toLocalDate());
    assertThat(mandate.isActive()).isTrue();
  }

  @Test
  void shouldRevokeMandateOnce() {
    mandate.revoke();

    assertThat(mandate.isActive()).isFalse();
    assertThatThrownBy(mandate::revoke).isExactlyInstanceOf(MandateNotActiveException.class);
  }

  @Test
  void shouldRefundCollectedDirectDebitWithin8Weeks() {
    var debit = DirectDebit.collected(mandate, euros("80"), MONDAY_MORNING);

    debit.refund(MONDAY_MORNING.plusWeeks(8));

    assertThat(debit.status()).isEqualTo(DirectDebitStatus.REFUNDED);
    assertThatThrownBy(() -> debit.refund(MONDAY_MORNING)).isExactlyInstanceOf(DirectDebitNotRefundableException.class);
  }

  @Test
  void shouldNotRefundAfter8Weeks() {
    var debit = DirectDebit.collected(mandate, euros("80"), MONDAY_MORNING);

    assertThatThrownBy(() -> debit.refund(MONDAY_MORNING.plusWeeks(8).plusMinutes(1))).isExactlyInstanceOf(DirectDebitNotRefundableException.class);
  }

  @Test
  void shouldNotRefundRejectedDirectDebit() {
    var debit = DirectDebit.rejected(mandate, euros("80"), MONDAY_MORNING);

    assertThat(debit.status()).isEqualTo(DirectDebitStatus.REJECTED);
    assertThat(debit.creditor()).isEqualTo("EDF");
    assertThat(debit.account()).isEqualTo(PAYER);
    assertThat(debit.amount()).isEqualByComparingTo("80");
    assertThat(debit.collectedAt()).isEqualTo(MONDAY_MORNING);
    assertThatThrownBy(() -> debit.refund(MONDAY_MORNING)).isExactlyInstanceOf(DirectDebitNotRefundableException.class);
  }
}
