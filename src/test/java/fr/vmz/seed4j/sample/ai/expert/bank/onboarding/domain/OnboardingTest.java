package fr.vmz.seed4j.sample.ai.expert.bank.onboarding.domain;

import static fr.vmz.seed4j.sample.ai.expert.bank.onboarding.domain.OnboardingFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import fr.vmz.seed4j.sample.ai.UnitTest;
import fr.vmz.seed4j.sample.ai.shared.bank.domain.CustomerId;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

@UnitTest
class OnboardingTest {

  private final CustomersRepository customers = mock(CustomersRepository.class, CALLS_REAL_METHODS);
  private final AccountFreezes freezes = mock(AccountFreezes.class);
  private final Onboarding onboarding = new Onboarding(customers, freezes);

  @Test
  void shouldOnboardProspect() {
    var customer = onboarding.onboard(adult(), TODAY);

    verify(customers).save(customer);
  }

  @Test
  void shouldNotSaveIncompleteFile() {
    assertThatThrownBy(() -> onboarding.onboard(withDocuments(TODAY, TODAY, false), TODAY)).isExactlyInstanceOf(IncompleteFileException.class);
    verify(customers, never()).save(any());
  }

  @Test
  void shouldFreezeAccountsOfOverdueReviews() {
    var overdue = customer();
    var upToDate = new ProspectToOnboard("John Doe", overdue.birthDate(), overdue.monthlyIncome(), TODAY.plusYears(10), TODAY.plusYears(2), true, false, RiskLevel.LOW, null)
      .onboard(TODAY.plusYears(2));
    doReturn(List.of(overdue, upToDate)).when(customers).all();

    onboarding.freezeOverdueReviews(TODAY.plusYears(3).plusDays(1));

    verify(freezes).freezeAccountsOf(overdue.id());
    verify(freezes, never()).freezeAccountsOf(upToDate.id());
  }

  @Test
  void shouldUnfreezeAccountsOnReview() {
    var customer = known(customer());

    onboarding.review(customer.id(), RiskLevel.MEDIUM, TODAY.plusYears(4));

    assertThat(customer.risk()).isEqualTo(RiskLevel.MEDIUM);
    verify(customers).save(customer);
    verify(freezes).unfreezeAccountsOf(customer.id());
  }

  @Test
  void shouldNotReviewUnknownCustomer() {
    var unknown = CustomerId.newId();
    doReturn(Optional.empty()).when(customers).get(unknown);

    assertThatThrownBy(() -> onboarding.customer(unknown)).isExactlyInstanceOf(UnknownCustomerException.class);
  }

  private Customer known(Customer customer) {
    doReturn(Optional.of(customer)).when(customers).get(customer.id());

    return customer;
  }
}
