package fr.vmz.seed4j.sample.ai.expert.rental.loyalty.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.expert.rental.loyalty.domain.LoyaltyAccount;
import fr.vmz.seed4j.sample.ai.expert.rental.loyalty.domain.LoyaltyAccountsRepository;
import fr.vmz.seed4j.sample.ai.expert.rental.loyalty.domain.LoyaltyStatus;
import fr.vmz.seed4j.sample.ai.shared.rental.domain.Email;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;

@UnitTest
class LoyaltyApplicationServiceTest {

  private static final Email JANE = new Email("jane@mail.com");

  private final LoyaltyAccountsRepository accounts = mock(LoyaltyAccountsRepository.class);
  private final LoyaltyApplicationService loyalty = new LoyaltyApplicationService(
    accounts,
    Clock.fixed(Instant.parse("2026-06-01T08:00:00Z"), ZoneId.of("Europe/Paris"))
  );

  @Test
  void shouldCreditRedeemAndCancelPointsToday() {
    var account = LoyaltyAccount.open(JANE);
    doReturn(Optional.of(account)).when(accounts).get(JANE);
    var reservation = UUID.randomUUID();

    loyalty.credit(JANE, 3200, "contract-1");
    loyalty.redeemFreeDay(JANE, reservation);
    assertThat(loyalty.account(JANE).balanceOn(loyalty.today())).isEqualTo(2700);
    loyalty.cancelRedemption(JANE, reservation);

    assertThat(loyalty.today()).isEqualTo(LocalDate.of(2026, 6, 1));
    assertThat(account.balanceOn(loyalty.today())).isEqualTo(3200);
    assertThat(loyalty.statusOf(JANE)).isEqualTo(LoyaltyStatus.GOLD);
  }
}
