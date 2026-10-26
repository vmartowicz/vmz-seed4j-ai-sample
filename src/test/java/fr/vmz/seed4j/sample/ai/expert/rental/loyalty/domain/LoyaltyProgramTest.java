package fr.vmz.seed4j.sample.ai.expert.rental.loyalty.domain;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.shared.rental.domain.Email;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@UnitTest
class LoyaltyProgramTest {

  private static final LocalDate TODAY = LocalDate.of(2026, 6, 1);
  private static final Email JANE = new Email("jane@mail.com");

  private final LoyaltyAccountsRepository accounts = mock(LoyaltyAccountsRepository.class);
  private final LoyaltyProgram program = new LoyaltyProgram(accounts);

  @BeforeEach
  void loadAccounts() {
    doReturn(Optional.empty()).when(accounts).get(any());
  }

  @Test
  void shouldOpenAccountOnFirstCredit() {
    program.credit(JANE, 300, TODAY, "contract-1");

    verify(accounts).save(argThat(account -> account.customer().equals(JANE) && account.balanceOn(TODAY) == 300));
  }

  @Test
  void shouldCreditExistingAccount() {
    var account = known(LoyaltyAccount.open(JANE));

    program.credit(JANE, 300, TODAY, "contract-1");

    assertThat(account.balanceOn(TODAY)).isEqualTo(300);
    verify(accounts).save(account);
  }

  @Test
  void shouldGiveMemberStatusToUnknownCustomers() {
    assertThat(program.statusOf(JANE, TODAY)).isEqualTo(LoyaltyStatus.MEMBER);
    assertThat(program.account(JANE).balanceOn(TODAY)).isZero();
  }

  @Test
  void shouldRedeemAndCancelFreeDay() {
    var account = known(LoyaltyAccount.open(JANE));
    account.credit(3000, TODAY, "contract-1");
    var reservation = UUID.randomUUID();

    program.redeemFreeDay(JANE, reservation, TODAY);
    assertThat(account.balanceOn(TODAY)).isEqualTo(2500);
    assertThat(program.statusOf(JANE, TODAY)).isEqualTo(LoyaltyStatus.GOLD);
    program.cancelRedemption(JANE, reservation);

    assertThat(account.balanceOn(TODAY)).isEqualTo(3000);
    verify(accounts, times(2)).save(account);
  }

  @Test
  void shouldNotRedeemWithoutAccount() {
    assertThatThrownBy(() -> program.redeemFreeDay(JANE, UUID.randomUUID(), TODAY)).isExactlyInstanceOf(NotEnoughPointsException.class);
  }

  @Test
  void shouldIgnoreCancellationWithoutAccount() {
    program.cancelRedemption(JANE, UUID.randomUUID());

    verify(accounts, never()).save(any());
  }

  private LoyaltyAccount known(LoyaltyAccount account) {
    doReturn(Optional.of(account)).when(accounts).get(account.customer());

    return account;
  }
}
